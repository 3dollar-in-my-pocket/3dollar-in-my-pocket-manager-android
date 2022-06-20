package app.threedollars.manager.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import app.threedollars.common.ui.PinkOpacity20
import app.threedollars.manager.R
import app.threedollars.manager.getActivity
import app.threedollars.manager.home.content.AddressRoundTextViewContent
import app.threedollars.manager.home.content.CurrentLocationButtonContent
import app.threedollars.manager.home.content.SalesLayoutContent
import app.threedollars.manager.viewModels.HomeViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

@OptIn(ExperimentalNaverMapApi::class)
@Preview
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(locationTrackingMode = LocationTrackingMode.Follow)
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isZoomControlEnabled = false)
        )
    }
    var currentPosition by remember {
        mutableStateOf(
            LatLng(37.56, 126.97)
        )
    }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(currentPosition, 15.0)
    }
    val markersStateList = remember {
        mutableStateListOf<MarkerState>()
    }

    initViewModels(viewModel, lifecycleOwner, markersStateList)

    viewModel.getMyStore("e9a1708e-3c2a-4dd4-a89e-58a85b5d1f75")

    ConstraintLayout {
        val (imageButton, box, bottomLayout) = createRefs()
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            locationSource = context.getActivity()?.let { FusedLocationSource(it, 1000) },
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
        ) {
            markersStateList.forEach {
                Marker(
                    state = MarkerState(it.position),
                    icon = OverlayImage.fromResource(R.drawable.ic_gps)
                )
            }
            CircleOverlay(center = currentPosition, color = PinkOpacity20, radius = 150.0)
        }
        AddressRoundTextViewContent(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(54.dp)
                .fillMaxWidth()
                .constrainAs(box) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top, margin = 44.dp)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(White),
            setLatLng = {
                currentPosition = it
            },
            currentPosition = currentPosition
        )

        CurrentLocationButtonContent(Modifier.constrainAs(imageButton) {
            end.linkTo(parent.end, margin = 16.dp)
            bottom.linkTo(bottomLayout.top, margin = 16.dp)
        }, cameraPositionState) {
            currentPosition = it
            cameraPositionState.move(CameraUpdate.scrollTo(currentPosition))
        }

        SalesLayoutContent(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
                .constrainAs(bottomLayout) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                }
                .clip(RoundedCornerShape(16.dp))
                .background(White),
            mapUiSettings = mapUiSettings
        )
    }
}

private fun initViewModels(
    viewModel: HomeViewModel,
    lifecycleOwner: LifecycleOwner,
    markerStateList: SnapshotStateList<MarkerState>
) {
    viewModel.storesAroundResponseList.observe(lifecycleOwner) { storesAroundResponseList ->
        val latLngList = storesAroundResponseList.map { storesAroundResponse ->
            MarkerState(
                LatLng(
                    storesAroundResponse.location?.latitude!!,
                    storesAroundResponse.location?.longitude!!
                )
            )
        }
        markerStateList.clear()
        markerStateList.addAll(latLngList)
    }
}