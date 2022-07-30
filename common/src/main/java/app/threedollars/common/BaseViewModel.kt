package app.threedollars.common

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("error", throwable.message.toString())
    }

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