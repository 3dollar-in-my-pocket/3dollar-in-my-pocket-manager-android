package app.threedollars.manager.home.content

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import app.threedollars.manager.R
import app.threedollars.manager.getActivity
import app.threedollars.manager.home.moveToCurrentLocation
import app.threedollars.manager.requestPermissionIfNeeds
import com.naver.maps.map.compose.CameraPositionState

@Composable
fun CurrentLocationButtonContent(
    modifier: Modifier, cameraPositionState: CameraPositionState
) {
    val activity = LocalContext.current.getActivity()
    IconButton(
        modifier = modifier,
        onClick = {
            activity?.requestPermissionIfNeeds()
            moveToCurrentLocation(activity, cameraPositionState)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_current_location),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}