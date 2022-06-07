package app.threedollars.manager.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import app.threedollars.manager.*
import app.threedollars.manager.R
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
        val (imageButton, box, bottomLayout) = createRefs()

        AndroidView(factory = { mapView!! })

        AddressRoundTextView(
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .height(54.dp)
                .fillMaxWidth()
                .constrainAs(box) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 44.dp)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.white))
        )

        CurrentLocationButton(Modifier.constrainAs(imageButton) {
            end.linkTo(parent.end, margin = 16.dp)
            bottom.linkTo(bottomLayout.top, margin = 16.dp)
        })

        SalesLayout(
            Modifier
                .padding(bottom = 50.dp)
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
                .constrainAs(bottomLayout) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.white))
        )
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

// TODO: 테스트 필요 버그확률 높음
@Composable
fun AddressRoundTextView(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val latLng = remember { mutableStateOf(currentPosition) }

        val activity = LocalContext.current.getActivity()
        val fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }
        if (isLocationAvailable() && isGpsAvailable()) {
            val locationResult = fusedLocationProviderClient?.lastLocation
            locationResult?.addOnSuccessListener {
                if (it != null) {
                    latLng.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
        Text(
            text = getCurrentLocationName(latLng.value)
                ?: stringResource(id = R.string.location_no_address),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SalesLayout(modifier: Modifier) {
    val sale = remember { mutableStateOf(false) }
    Column(
        modifier = modifier.background(
            color = colorResource(id = if (sale.value) R.color.green500 else R.color.white)
        )
    ) {
        Row(modifier = Modifier.padding(start = 26.dp, top = 24.dp)) {
            Text(
                text = if (sale.value) "오늘은" else "지금 계신 위치에서 영업을 시작할까요?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(id = if (sale.value) R.color.white else R.color.black)
            )
            if (sale.value) {
                Text(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.green400)),
                    text = "5시간 24분 23초",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.white)
                )
            }
        }
        Text(
            modifier = Modifier.padding(start = 26.dp, top = 8.dp),
            text = if (sale.value) "동안 영업중이시네요! 오늘도 대박나세요!" else "영업을 시작하면 위치가 손님들에게 공개됩니다.",
            fontWeight = if (sale.value) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (sale.value) 18.sp else 14.sp,
            color = colorResource(id = if (sale.value) R.color.white else R.color.black)
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = if (sale.value) R.color.white else R.color.green500)),
            onClick = { sale.value = !sale.value },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = if (sale.value) "셔터 내리기" else "오늘의 영업 시작하기",
                fontSize = 16.sp,
                color = colorResource(id = if (sale.value) R.color.green500 else R.color.white)
            )
        }
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