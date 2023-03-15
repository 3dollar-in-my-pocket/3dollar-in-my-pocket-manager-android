package app.threedollars.manager.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.threedollars.common.ui.Pink_op20
import app.threedollars.manager.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun CommonMapScreen(currentLatLng: LatLng, markers: List<Marker>) {
    NaverMap(
        modifier = Modifier,
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(locationTrackingMode = LocationTrackingMode.Follow),
        uiSettings = MapUiSettings(isZoomControlEnabled = false, isLocationButtonEnabled = false),
        onLocationChange = {

        },
    ) {
        if (!currentLatLng.isValid) {
            LocationOverlay(
                position = currentLatLng,
                icon = OverlayImage.fromResource(R.drawable.ic_my_location_marker),
                circleRadius = 148.dp,
                circleColor = Pink_op20
            )
        }
        markers.forEach { marker -> marker }
    }
}