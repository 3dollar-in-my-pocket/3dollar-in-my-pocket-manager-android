package app.threedollars.manager.splash

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.repository.SignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * 1. enum 캐싱
 * 2. Token 상태에 따라 첫화면 결정 (소셜 로그인 or 홈화면 or 승인대기)
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val signRepository: SignRepository
) : BaseViewModel() {

    private val _isLogin = MutableEventFlow<Boolean>()
    val isLogin: EventFlow<Boolean>
        get() = _isLogin

    private val _needSignUp = MutableEventFlow<Boolean>()
    val needSignUp: EventFlow<Boolean>
        get() = _needSignUp

    init {
        viewModelScope.launch(exceptionHandler) {
            tryLogin()
        }
    }

    private suspend fun tryLogin() {
        val accessToken = signRepository.getAccessToken().firstOrNull()
        if (accessToken.isNullOrBlank()) {
            _isLogin.emit(false)
            return
        }

        signRepository.loginWithKakao {
            viewModelScope.launch {
                if (it.isSuccess && !it.getOrNull().isNullOrBlank()) {
                    _isLogin.emit(true)
                } else {
                    _needSignUp.emit(true)
                }
            }
        }
    }
}