package app.threedollars.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MaintenanceStateManager {
    private val _isMaintenanceMode = MutableStateFlow(false)
    val isMaintenanceMode: StateFlow<Boolean> = _isMaintenanceMode.asStateFlow()

    private var lastMaintenanceTime: Long = 0

    fun setMaintenanceMode(isInMaintenance: Boolean) {
        if (isInMaintenance) {
            lastMaintenanceTime = System.currentTimeMillis()
        }
        _isMaintenanceMode.value = isInMaintenance
    }

    fun getLastMaintenanceTime(): Long = lastMaintenanceTime

    fun reset() {
        _isMaintenanceMode.value = false
        lastMaintenanceTime = 0
    }
}