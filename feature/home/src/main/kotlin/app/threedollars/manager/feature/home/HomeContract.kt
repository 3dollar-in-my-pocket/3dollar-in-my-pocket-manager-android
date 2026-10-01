package app.threedollars.manager.feature.home

import app.threedollars.domain.dto.StorePreferenceDto
import app.threedollars.manager.feature.home.model.BossStoreRetrieveAroundVo
import app.threedollars.manager.feature.home.model.BossStoreRetrieveVo
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap.DEFAULT_CAMERA_POSITION


internal data class HomeState(
    val openLocation: LatLng = DEFAULT_CAMERA_POSITION.target,
    val location: LatLng = DEFAULT_CAMERA_POSITION.target,
    val currentLocation: LatLng = DEFAULT_CAMERA_POSITION.target,
    val address: String = "",
    val bossStoreRetrieveMe: BossStoreRetrieveVo = BossStoreRetrieveVo(),
    val bossStoreRetrieveArounds: List<BossStoreRetrieveAroundVo> = listOf(),
    val autoOpenClose: AutoOpenCloseState = AutoOpenCloseState(),
)

internal data class AutoOpenCloseState(
    val autoOpenCloseControl: Boolean = false,
    val showAlert: Boolean = false,
) {
    val shutterAction: ShutterAction
        get() = if (autoOpenCloseControl) ShutterAction.SHOW_AUTO_OPEN_CLOSE_ALERT else ShutterAction.CLOSE_STORE

    fun withPreference(preference: StorePreferenceDto) = copy(autoOpenCloseControl = preference.autoOpenCloseControl)

    fun onShutterClick() = when (shutterAction) {
        ShutterAction.SHOW_AUTO_OPEN_CLOSE_ALERT -> copy(showAlert = true)
        ShutterAction.CLOSE_STORE -> this
    }

    fun dismissAlert() = copy(showAlert = false)
}

internal enum class ShutterAction {
    CLOSE_STORE,
    SHOW_AUTO_OPEN_CLOSE_ALERT,
}

internal enum class StoreStateType {
    OPEN,
    CLOSE
}
