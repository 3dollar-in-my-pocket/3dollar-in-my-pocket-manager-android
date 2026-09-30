package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.util.catchOrCancel
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.domain.usecase.BossAccountUseCase
import app.threedollars.domain.usecase.BossDeviceUseCase
import app.threedollars.domain.usecase.GetAppStatusUseCase
import app.threedollars.manager.sign.LoginNavItem
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val bossAccountUseCase: BossAccountUseCase,
    private val bossDeviceUseCase: BossDeviceUseCase,
    private val getAppStatusUseCase: GetAppStatusUseCase,
) : BaseViewModel() {

    private val _effect = Channel<SplashEffect>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect = _effect.receiveAsFlow()

    override fun onCoroutineException(context: CoroutineContext, throwable: Throwable) {
        super.onCoroutineException(context, throwable)
        _effect.trySend(SplashEffect.OnUnexpectedError)
    }

    init {
        checkAppStatus()
    }

    fun retry() {
        checkAppStatus()
    }

    private fun checkAppStatus() {
        viewModelScope.launch {
            catchOrCancel(
                onCatch = {
                    _effect.send(SplashEffect.OnUnexpectedError)
                }
            ) {
                when (val result = getAppStatusUseCase().toAppStatusResult()) {
                    AppStatusResult.Proceed -> autoLogin()
                    is AppStatusResult.ForceUpdate -> _effect.send(SplashEffect.OnForceUpdate(result))
                    AppStatusResult.Maintenance -> Unit
                    AppStatusResult.Error -> _effect.send(SplashEffect.OnUnexpectedError)
                }
            }
        }
    }

    private fun login(accessToken: String) {
        viewModelScope.launch(exceptionHandler) {
            catchOrCancel(
                onCatch = {
                    _effect.send(SplashEffect.OnUnexpectedError)
                }
            ) {
                authUseCase.login("KAKAO", accessToken).collect {
                    when (it.code.toString()) {
                        "200" -> {
                            it.data?.token?.let { token ->
                                authUseCase.saveAccessToken(token).collect {
                                    checkMyInfo()
                                }
                            }
                        }
                        "503" -> {
                            // 503 에러는 MaintenanceInterceptor에서 처리됨
                            // 화면 전환 없이 점검 화면만 표시
                        }
                        "404", "400" -> {
                            _effect.send(SplashEffect.OnNavigate(LoginNavItem.Login))
                        }
                        else -> {
                            _effect.send(SplashEffect.OnUnexpectedError)
                        }
                    }
                }
            }
        }
    }

    private fun checkMyInfo() {
        viewModelScope.launch(exceptionHandler) {
            catchOrCancel(
                onCatch = {
                    _effect.send(SplashEffect.OnUnexpectedError)
                }
            ) {
                bossAccountUseCase.getBossAccount().collect {
                    when (it.code.toString()) {
                        "200" -> {
                            val firebaseToken = async { FirebaseMessaging.getInstance().token.await() }
                            bossDeviceUseCase.putBossDeviceToken("FCM", firebaseToken.await()).collect {
                                _effect.send(SplashEffect.OnNavigate(LoginNavItem.Home))
                            }
                        }
                        "503" -> {
                            // 503 에러는 MaintenanceInterceptor에서 처리됨
                            // 화면 전환 없이 점검 화면만 표시
                        }
                        "401" -> {
                            autoLogin()
                        }
                        "403" -> {
                            _effect.send(SplashEffect.OnNavigate(LoginNavItem.Waiting))
                        }
                        "404" -> {
                            _effect.send(SplashEffect.OnNavigate(LoginNavItem.Login))
                        }
                        else -> {
                            _effect.send(SplashEffect.OnUnexpectedError)
                        }
                    }
                }
            }
        }
    }

    private fun autoLogin() {
        viewModelScope.launch {
            catchOrCancel(
                onCatch = {
                    _effect.send(SplashEffect.OnUnexpectedError)
                }
            ) {
                val demoCode = authUseCase.getDemoCode().firstOrNull()?.data

                if (!demoCode.isNullOrBlank()) {
                    demoLogin(demoCode)
                } else {
                    val token = authUseCase.getSocialAccessToken().firstOrNull()?.data

                    if (token.isNullOrBlank()) {
                        _effect.send(SplashEffect.OnNavigate(LoginNavItem.Login))
                    } else {
                        login(token)
                    }
                }
            }
        }
    }

    private fun demoLogin(code: String) {
        viewModelScope.launch(exceptionHandler) {
            catchOrCancel(
                onCatch = {
                    _effect.send(SplashEffect.OnUnexpectedError)
                }
            ) {
                authUseCase.demoLogin(code).collect {
                    when (it.code.toString()) {
                        "200" -> {
                            it.data?.token?.let { token ->
                                authUseCase.saveAccessToken(token).collect {
                                    checkMyInfo()
                                }
                            }
                        }
                        "503" -> {
                            // 503 에러는 MaintenanceInterceptor에서 처리됨
                        }
                        "404", "400" -> {
                            _effect.send(SplashEffect.OnNavigate(LoginNavItem.Login))
                        }
                        else -> {
                            _effect.send(SplashEffect.OnUnexpectedError)
                        }
                    }
                }
            }
        }
    }
}
