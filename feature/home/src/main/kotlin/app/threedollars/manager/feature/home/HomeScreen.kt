package app.threedollars.manager.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ext.toStringDefault
import app.threedollars.manager.feature.home.components.HomeBottomOff
import app.threedollars.manager.feature.home.components.HomeBottomOn
import app.threedollars.manager.feature.home.components.MapView
import app.threedollars.manager.feature.home.model.BossStoreRetrieveAroundVo
import app.threedollars.manager.feature.home.model.BossStoreRetrieveVo
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState

@Composable
internal fun HomeScreen(
    location: LatLng,
    openLocation: LatLng,
    currentLocation: LatLng,
    address: String,
    bossStoreRetrieveMe: BossStoreRetrieveVo,
    cameraPositionState: CameraPositionState,
    bossStoreRetrieveArounds: List<BossStoreRetrieveAroundVo>,
    onStoreStateUpdate: (StoreStateType, LatLng) -> Unit,
    onCurrentLocationClick: () -> Unit
) {

    var isFoodTruckCheck by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
        ) {
            MapView(
                modifier = Modifier,
                cameraPositionState = cameraPositionState,
                openLocation = openLocation,
                currentLocation = currentLocation,
                isFoodTruckCheck = isFoodTruckCheck,
                bossStoreArounds = bossStoreRetrieveArounds,
                openStatus = bossStoreRetrieveMe.openStatus.status
            )
            Text(
                text = address,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 44.dp)
                    .height(56.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .wrapContentHeight(Alignment.CenterVertically),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 8.dp, bottom = 32.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .clickable { isFoodTruckCheck = !isFoodTruckCheck }
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(start = 12.dp, end = 11.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = if (isFoodTruckCheck) R.drawable.ic_check else R.drawable.ic_uncheck),
                        contentDescription = "체크박스"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "다른 푸드트럭 보기",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.gray100)
                    )
                }
                IconButton(onClick = onCurrentLocationClick) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "내 위치 아이콘"
                    )
                }

            }
        }
        if (bossStoreRetrieveMe.openStatus.status == StoreStateType.OPEN) {
            HomeBottomOn(
                Modifier.align(Alignment.BottomStart),
                bossStoreRetrieveMe.openStatus.openStartDateTime.toStringDefault()
            ) {
                onStoreStateUpdate(StoreStateType.CLOSE, cameraPositionState.position.target)
            }
        } else {
            HomeBottomOff(Modifier.align(Alignment.BottomStart)) {
                if (location.latitude != 0.0) {
                    onStoreStateUpdate(StoreStateType.OPEN, cameraPositionState.position.target)
                }
            }
        }
    }
}
