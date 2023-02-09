package app.threedollars.manager.sign.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    private val _isLogin = MutableEventFlow<Boolean>()
    val isLogin: EventFlow<Boolean> get() = _isLogin
    fun login(accessToken: String) {
        viewModelScope.launch(exceptionHandler) {
            authUseCase.login("KAKAO", accessToken).collect {

                if (it.code.toString() == "200") {
                    _isLogin.emit(true)
                } else if (it.code.toString() == "404") {
                    _isLogin.emit(false)
                }
            }
        }
    }
}