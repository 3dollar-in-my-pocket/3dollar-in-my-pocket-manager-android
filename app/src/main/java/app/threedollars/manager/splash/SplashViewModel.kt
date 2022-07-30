package app.threedollars.manager.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.data.oauth.KakaoLogin
import app.threedollars.domain.repository.SignRepository
import app.threedollars.manager.BottomNavItem
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
    private val kakaoLogin: KakaoLogin,
    private val signRepository: SignRepository
) : BaseViewModel() {

    private val _nextScreen = MutableEventFlow<BottomNavItem>()
    val nextScreen: EventFlow<BottomNavItem>
        get() = _nextScreen

    init {
        viewModelScope.launch(exceptionHandler) {
            tryLogin()
        }
    }

    private suspend fun tryLogin() {
        val accessToken = signRepository.getAccessToken().firstOrNull()
        val refreshToken = signRepository.getAccessToken().firstOrNull() ?: ""

        if (accessToken.isNullOrBlank()) {
            Log.e("tryLogin", "No accessToken")
            _nextScreen.emit(BottomNavItem.LoginScreen)
            return
        }

        val refreshResult = kakaoLogin.refreshToken(refreshToken)
        if (refreshResult == null) {
            _nextScreen.emit(BottomNavItem.LoginScreen)
            return
        }

        signRepository.saveKakaoRefreshToken(refreshResult.refreshToken)
        signRepository.saveKakaoRefreshToken(refreshResult.refreshToken)
        signRepository.loginWithKakao(refreshResult.accessToken) {
            viewModelScope.launch(exceptionHandler) {
                if (it.isSuccess && !it.getOrNull().isNullOrBlank()) {
                    Log.d("login success", "token : ${it.getOrNull()}")
                    signRepository.saveAccessToken(it.getOrNull() ?: "")
                    _nextScreen.emit(BottomNavItem.HomeScreen)
                } else {
                    Log.e("login failed", "error : ${it.getOrNull()}")
                    _nextScreen.emit(BottomNavItem.SignUpFormScreen)
                }
            }
        }
    }
}