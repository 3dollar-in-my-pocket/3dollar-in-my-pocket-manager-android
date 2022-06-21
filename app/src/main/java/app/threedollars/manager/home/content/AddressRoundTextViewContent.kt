package app.threedollars.manager.home.content

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.threedollars.data.store.request.StoresAroundRequest
import app.threedollars.manager.*
import app.threedollars.manager.home.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng

// TODO: 테스트 필요 버그확률 높음
@SuppressLint("MissingPermission")
@Composable
fun AddressRoundTextViewContent(
    modifier: Modifier, viewModel: HomeViewModel = hiltViewModel(), setLatLng: (LatLng)->Unit, currentPosition : LatLng
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        val activity = LocalContext.current.getActivity()
        val fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }
        if (isLocationAvailable() && isGpsAvailable()) {
            val locationResult = fusedLocationProviderClient?.lastLocation
            locationResult?.addOnSuccessListener {
                if (it != null) {
                    setLatLng(LatLng(it.latitude, it.longitude))
                    viewModel.getStoresAround(
                        "Bearer e9a1708e-3c2a-4dd4-a89e-58a85b5d1f75",
                        storesAroundRequest = StoresAroundRequest(
                            mapLongitude = it.longitude.toString(),
                            mapLatitude = it.latitude.toString()
                        )
                    )
                }
            }
        }
        Text(
            text = getCurrentLocationName(currentPosition)
                ?: stringResource(id = R.string.location_no_address),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}