package app.threedollars.manager.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.naver.maps.map.MapView
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Lifecycle 이벤트를 수신하기 위해 AndroidView의 밖에서 먼저 선언합니다.
    // recomposition시에도 유지되어야 하기 때문에 remember { } 로 기억합니다.
    val mapView = remember {
        MapView(context).apply {
            getMapAsync { naverMap ->
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
                    Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                    Lifecycle.Event.ON_START -> mapView.onStart()
                    Lifecycle.Event.ON_RESUME -> mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                    Lifecycle.Event.ON_STOP -> mapView.onStop()
                    Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                    else -> {}
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
    AndroidView(factory = { mapView })
}