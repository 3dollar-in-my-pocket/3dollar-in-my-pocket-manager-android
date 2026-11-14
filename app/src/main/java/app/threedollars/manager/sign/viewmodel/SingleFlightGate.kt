package app.threedollars.manager.sign.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SingleFlightGate {

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    fun tryEnter(): Boolean = _isRunning.compareAndSet(expect = false, update = true)

    fun exit() {
        _isRunning.value = false
    }
}
