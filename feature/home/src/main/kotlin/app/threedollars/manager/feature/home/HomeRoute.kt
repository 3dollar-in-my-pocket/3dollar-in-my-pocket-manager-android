package app.threedollars.manager.feature.home

import android.Manifest
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
        address = uiState.address,
        bossStoreRetrieveMe = uiState.bossStoreRetrieveMe,
        bossStoreRetrieveArounds = uiState.bossStoreRetrieveArounds,
        onAddressUpdate = viewModel::updateAddress,
        onStoreStateUpdate = { storeStateType, location ->
            when (storeStateType) {
                StoreStateType.OPEN -> {
                    viewModel.storeOpen(location = location)
                }

                StoreStateType.CLOSE -> {
                    viewModel.storeClosed()
                }
            }
        },
        onCurrentLocationClick = {
            if (uiState.bossStoreRetrieveMe.openStatus.status == StoreStateType.OPEN) {
                viewModel.getBossStoreAround(uiState.location)
            } else {
                currentLocationState(
                    context = context,
                    fusedLocationClient = fusedLocationClient,
                    onCurrentLocation = viewModel::getBossStoreAround
                )
            }
        }
    )
}
