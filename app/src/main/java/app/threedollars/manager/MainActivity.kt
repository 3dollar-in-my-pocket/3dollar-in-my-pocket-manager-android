package app.threedollars.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.naver.maps.map.MapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NaverMapComposable()
        }
    }
}

@Composable
fun LoginButtons() {
    Column(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.padding(bottom = 30.dp)) {
            Text(
                text = "우리 앱의 대표 그래픽",
                style = TextStyle(color = Color.Black)
            )
        }
        Row() {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "카카오 계정으로 로그인")
            }
        }
        Row() {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "네이버 계정으로 로그인")
            }
        }
    }
}
@Composable
fun NaverMapComposable() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Lifecycle 이벤트를 수신하기 위해 AndroidView의 밖에서 먼저 선언합니다.
    // recomposition시에도 유지되어야 하기 때문에 remember { } 로 기억합니다.
    val mapView = remember {
        MapView(context).apply {
            getMapAsync { naverMap ->
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