package app.threedollars.manager.feature.home.components


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.threedollars.common.ui.Pink_OP20
import app.threedollars.manager.feature.home.R
import app.threedollars.manager.feature.home.StoreStateType
import app.threedollars.manager.feature.home.model.BossStoreRetrieveAroundVo
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.OverlayImage

@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun MapView(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    openLocation: LatLng,
    currentLocation: LatLng,
    isFoodTruckCheck: Boolean,
    bossStoreArounds: List<BossStoreRetrieveAroundVo>,
    openStatus: StoreStateType,
) {
    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(locationTrackingMode = LocationTrackingMode.None),
        uiSettings = MapUiSettings(isZoomControlEnabled = false, isLocationButtonEnabled = false),
    ) {
        if(openStatus == StoreStateType.OPEN) {
            Marker(
                state = MarkerState(openLocation),
                icon = OverlayImage.fromResource(R.drawable.ic_marker)
            )
            CircleOverlay(center = currentLocation, color = Pink_OP20, radius = 110.0)
        }else{
            Marker(
                state = MarkerState(cameraPositionState.position.target),
                icon = OverlayImage.fromResource(R.drawable.ic_marker)
            )
            CircleOverlay(center = currentLocation, color = Pink_OP20, radius = 110.0)
        }
        if (isFoodTruckCheck) {
            bossStoreArounds.forEach {
                Marker(
                    state = MarkerState(LatLng(it.location.latitude, it.location.longitude)),
                    icon = OverlayImage.fromResource(R.drawable.ic_marker_disable)
                )
            }
        }
    }
}