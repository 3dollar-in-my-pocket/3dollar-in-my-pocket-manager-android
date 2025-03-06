package app.threedollars.manager.feature.home

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.rememberCameraPositionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoute(
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        if (locationPermissionsState.allPermissionsGranted) {
            currentLocationState(
                context = context,
                fusedLocationClient = fusedLocationClient,
                onCurrentLocation = viewModel::getBossStoreAround
            )

        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
            viewModel.updateAddress("위치권한을 허락해주세요.")
        }
    }

    HomeScreen(
        location = uiState.location,
        openLocation = uiState.openLocation,
        currentLocation = uiState.currentLocation,
        address = uiState.address,
        bossStoreRetrieveMe = uiState.bossStoreRetrieveMe,
        cameraPositionState = cameraPositionState,
        bossStoreRetrieveArounds = uiState.bossStoreRetrieveArounds,
        onStoreStateUpdate = { storeStateType, location ->
            when (storeStateType) {
                StoreStateType.OPEN -> {
                    val distanceInMeters =
                        uiState.currentLocation.distanceTo(cameraPositionState.position.target)
                    if (distanceInMeters <= 100) {
                        viewModel.storeOpen(location = location)
                    } else {
                        Toast.makeText(
                            context,
                            "빨간 원(현재 위치의 반경 100m이내) 안에서 장사를 시작해주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                StoreStateType.CLOSE -> {
                    viewModel.storeClosed()
                }
            }
        },
        onCurrentLocationClick = {
            currentLocationState(
                context = context,
                fusedLocationClient = fusedLocationClient,
                onCurrentLocation = {
                    cameraPositionState.position =
                        CameraPosition(uiState.currentLocation, cameraPositionState.position.zoom)
                    viewModel.updateAddress(context.getCurrentLocationName(uiState.currentLocation))
                    viewModel.getBossStoreAround(it)
                }
            )
        }
    )
}
