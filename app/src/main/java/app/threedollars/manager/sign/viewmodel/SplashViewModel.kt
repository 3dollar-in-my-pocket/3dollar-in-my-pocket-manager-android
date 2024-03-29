package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AppConfigUseCase
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.domain.usecase.BossAccountUseCase
import app.threedollars.domain.usecase.BossDeviceUseCase
import app.threedollars.manager.BuildConfig
import app.threedollars.manager.sign.LoginNavItem
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val bossAccountUseCase: BossAccountUseCase,
    private val bossDeviceUseCase: BossDeviceUseCase,
    private val appConfigUseCase: AppConfigUseCase
) : BaseViewModel() {

    private val _loginNavItem = MutableEventFlow<LoginNavItem>()
    val loginNavItem: EventFlow<LoginNavItem> get() = _loginNavItem

    init {
        saveAppConfig()
        autoLogin()
    }

    private fun saveAppConfig() {
        viewModelScope.launch {
            appConfigUseCase.saveApplicationId(BuildConfig.APPLICATION_ID).collect()
            appConfigUseCase.saveVersionName(BuildConfig.VERSION_NAME).collect()
        }
    }

    private fun login(accessToken: String) {
        viewModelScope.launch(exceptionHandler) {
            authUseCase.login("KAKAO", accessToken).collect {
                if (it.code.toString() == "200") {
                    it.data?.token?.let { token ->
                        authUseCase.saveAccessToken(token).collect {
                            checkMyInfo()
                        }
                    }
                } else if (it.code.toString() == "404" || it.code.toString() == "400") {
                    _loginNavItem.emit(LoginNavItem.Login)
                }
            }
        }
    }

    private fun checkMyInfo() {
        viewModelScope.launch(exceptionHandler) {
            bossAccountUseCase.getBossAccount().collect {
                if (it.code.toString() == "200") {
                    val firebaseToken = async { FirebaseMessaging.getInstance().token.await() }
                    bossDeviceUseCase.putBossDeviceToken("FCM", firebaseToken.await()).collect {
                        _loginNavItem.emit(LoginNavItem.Home)
                    }
                } else if (it.code.toString() == "401") {
                    autoLogin()
                } else if (it.code.toString() == "403") {
                    _loginNavItem.emit(LoginNavItem.Waiting)
                } else if (it.code.toString() == "404") {
                    _loginNavItem.emit(LoginNavItem.Login)
                }
            }
        }
    }

    private fun autoLogin() {
        viewModelScope.launch {

            val token = authUseCase.getSocialAccessToken().firstOrNull()?.data

            if (token.isNullOrBlank()) {
                _loginNavItem.emit(LoginNavItem.Login)
            } else {
                login(token)
            }
        }
    }
}