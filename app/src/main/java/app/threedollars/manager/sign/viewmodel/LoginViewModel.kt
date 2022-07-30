package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.entity.SocialLoginToken
import app.threedollars.domain.repository.SignRepository
import app.threedollars.manager.BottomNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: SignRepository
) : BaseViewModel() {

    private val _nextScreen = MutableEventFlow<BottomNavItem>()
    val nextScreen: EventFlow<BottomNavItem>
        get() = _nextScreen

    fun startKakaoLogin(result: Result<SocialLoginToken>) {
        viewModelScope.launch(exceptionHandler) {
            val token = result.getOrNull()?.accessToken ?: ""

            repository.loginWithKakao(token) {
                takeLoginResult(it)
            }
        }
    }

    private fun takeLoginResult(it: Result<String?>) {
        viewModelScope.launch(exceptionHandler) {
            if (it.isSuccess) {
                repository.saveAccessToken(it.getOrNull() ?: "")
                _nextScreen.emit(BottomNavItem.HomeScreen)
            } else if (it.isFailure) {
                _nextScreen.emit(BottomNavItem.SignUpFormScreen)
            } else {
                // 오류 처리
            }
        }
    }
}