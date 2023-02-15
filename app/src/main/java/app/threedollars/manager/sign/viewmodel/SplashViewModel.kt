package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.domain.usecase.BossAccountUseCase
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val bossAccountUseCase: BossAccountUseCase
) : BaseViewModel() {

    private val _isLogin = MutableEventFlow<Boolean>()
    val isLogin: EventFlow<Boolean> get() = _isLogin

    init {
        autoLogin()
    }

    private fun login(accessToken: String) {
        viewModelScope.launch(exceptionHandler) {
            authUseCase.login("KAKAO", accessToken).collect {

                if (it.code.toString() == "200") {
                    it.data?.token?.let { token ->
                        authUseCase.saveAccessToken(token)
                    }
                    checkMyInfo()
                } else if (it.code.toString() == "404") {
                    _isLogin.emit(false)
                }
            }
        }
    }

    private fun checkMyInfo() {
        viewModelScope.launch(exceptionHandler) {
            bossAccountUseCase.getBossAccount().collect {
                if (it.code.toString() == "200") {
                    _isLogin.emit(true)
                } else if (it.code.toString() == "401") {
                    autoLogin()
                } else if (it.code.toString() == "403") {
                    // TODO: 가입 신청 대기 페이지로 이동
                } else if (it.code.toString() == "404") {
                    _isLogin.emit(false)
                }
            }
        }
    }

    private fun autoLogin() {
        viewModelScope.launch {
            val accessToken = TokenManager.instance.getToken()?.accessToken
            if (accessToken.isNullOrBlank()) {
                _isLogin.emit(false)
                return@launch
            } else {
                if (AuthApiClient.instance.hasToken()) {
                    UserApiClient.instance.accessTokenInfo { _, error ->
                        if (error == null) {
                            TokenManager.instance.getToken()?.accessToken?.let { login(it) }
                        } else {
                            viewModelScope.launch { _isLogin.emit(false) }
                        }
                    }
                } else {
                    _isLogin.emit(false)
                }
            }
        }
    }
}