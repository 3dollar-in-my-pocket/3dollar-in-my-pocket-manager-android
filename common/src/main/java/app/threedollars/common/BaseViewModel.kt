package app.threedollars.common

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _isLoading: MutableEventFlow<Boolean> = MutableEventFlow()
    val isLoading: EventFlow<Boolean> = _isLoading

    private val _toastMsg: MutableEventFlow<Int> = MutableEventFlow()
    val toastMsg: EventFlow<Int> = _toastMsg

    fun setLoading(value: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(value)
        }
    }

    fun setToastMsg(@StringRes resId: Int) {
        viewModelScope.launch {
            _toastMsg.emit(resId)
        }
    }
}