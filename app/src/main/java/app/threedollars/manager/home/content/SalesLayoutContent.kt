package app.threedollars.manager.home.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.threedollars.manager.home.screen.CloseSaleScreen
import app.threedollars.manager.home.screen.StartSaleScreen
import com.naver.maps.map.compose.MapUiSettings

@Composable
fun SalesLayoutContent(modifier: Modifier, mapUiSettings: MapUiSettings) {
    var startTime by rememberSaveable { mutableStateOf(0L) }
    var isSale by rememberSaveable { mutableStateOf(false) }

    if (isSale) {
        StartSaleScreen(modifier = modifier, startTime, isSale = isSale, startClick = {
            // TODO: 운영 종료 api 호출
            startTime = 0L
            isSale = !isSale
        })
    } else {
        CloseSaleScreen(modifier = modifier, closeClick = {
            // TODO: 운영 시작 api 호출
            startTime = System.currentTimeMillis()
            isSale = !isSale
        })
    }
    CenterMarkerContent(isSale)
    mapUiSettings.copy(
        isScrollGesturesEnabled = !isSale,
        isZoomGesturesEnabled = !isSale
    )
}