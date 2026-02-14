package app.threedollars.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MaintenanceStateManager {
    private val _showMaintenanceDialog = MutableStateFlow(false)
    val showMaintenanceDialog: StateFlow<Boolean> = _showMaintenanceDialog.asStateFlow()

    private var lastMaintenanceTime: Long = 0
    private var isDialogShowing = false

    fun setMaintenanceMode(isInMaintenance: Boolean) {
        if (isInMaintenance && !isDialogShowing) {
            // 다이얼로그가 이미 표시 중이 아닐 때만 새로 표시
            lastMaintenanceTime = System.currentTimeMillis()
            _showMaintenanceDialog.value = true
            isDialogShowing = true
        } else if (!isInMaintenance) {
            _showMaintenanceDialog.value = false
            isDialogShowing = false
        }
    }

    fun getLastMaintenanceTime(): Long = lastMaintenanceTime

    fun reset() {
        _showMaintenanceDialog.value = false
        isDialogShowing = false
        lastMaintenanceTime = 0
    }
}