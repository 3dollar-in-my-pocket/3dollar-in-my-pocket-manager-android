package app.threedollars.manager.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import app.threedollars.manager.*
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch


var currentPosition: LatLng? = null
var mapView: MapView? = null

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Lifecycle 이벤트를 수신하기 위해 AndroidView의 밖에서 먼저 선언합니다.
    // recomposition시에도 유지되어야 하기 때문에 remember { } 로 기억합니다.
    mapView = remember {
        MapView(context).apply {
            getMapAsync { naverMap ->
                naverMap.locationSource =
                    context.getActivity()?.let { FusedLocationSource(it, 1000) }
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
                naverMap.uiSettings.isZoomControlEnabled = false
            }
        }
    }

    // LifecycleEventObserver를 구현하고, 각 이벤트에 맞게 MapView의 Lifecycle 메소드를 호출합니다.
    val lifecycleObserver = remember {
        LifecycleEventObserver { source, event ->
            // CoroutineScope 안에서 호출해야 정상적으로 동작합니다.
            coroutineScope.launch {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> mapView?.onCreate(Bundle())
                    Lifecycle.Event.ON_START -> mapView?.onStart()
                    Lifecycle.Event.ON_RESUME -> mapView?.onResume()
                    Lifecycle.Event.ON_PAUSE -> mapView?.onPause()
                    Lifecycle.Event.ON_STOP -> mapView?.onStop()
                    Lifecycle.Event.ON_DESTROY -> mapView?.onDestroy()
                }
            }
        }
    }

    // 뷰가 해제될 때 이벤트 리스너를 제거합니다.
    DisposableEffect(true) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    // 생성된 MapView 객체를 AndroidView로 Wrapping 합니다.
    ConstraintLayout {

        val imageButton = createRef()

        AndroidView(factory = { mapView!! })
        CurrentLocationButton(Modifier.constrainAs(imageButton) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        })
    }
}

@Composable
fun CurrentLocationButton(modifier: Modifier) {
    val activity = LocalContext.current.getActivity()
    IconButton(
        modifier = modifier,
        onClick = {
            activity?.requestPermissionIfNeeds()
            moveToCurrentLocation(false, activity)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_current_location),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@SuppressLint("MissingPermission")
fun moveToCurrentLocation(
    showAnim: Boolean = false,
    activity: AppCompatActivity?
) {
    try {
        val fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }
        if (isLocationAvailable() && isGpsAvailable()) {
            val locationResult = fusedLocationProviderClient?.lastLocation
            locationResult?.addOnSuccessListener {
                if (it != null) {
                    currentPosition = LatLng(it.latitude, it.longitude)
                    currentPosition?.let { position ->
                        if (showAnim) {
                            moveCameraWithAnim(position)
                        } else {
                            moveCamera(position)
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("currentLocation", e.message ?: "")
        moveCamera(LatLng(37.56, 126.97))
    }
}

fun moveCamera(position: LatLng) {
    mapView?.getMapAsync { naverMap ->
        val cameraUpdate = CameraUpdate.scrollTo(position)
        naverMap.moveCamera(cameraUpdate)
    }
}

fun moveCameraWithAnim(position: LatLng) {
    mapView?.getMapAsync { naverMap ->
        val cameraUpdate = CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

}