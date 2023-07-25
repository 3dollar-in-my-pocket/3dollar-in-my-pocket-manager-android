package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.manager.sign.LoginNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel @Inject constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    private val _loginNavItem = MutableEventFlow<LoginNavItem>()
    val loginNavItem: EventFlow<LoginNavItem> get() = _loginNavItem

    fun logout() {
        viewModelScope.launch(exceptionHandler) {
            authUseCase.logout().collect {
                val accessTokenDeferred = async { authUseCase.saveAccessToken("") }
                val socialAccessTokenDeferred = async { authUseCase.saveSocialAccessToken("") }
                awaitAll(accessTokenDeferred, socialAccessTokenDeferred).forEach { flow ->
                    flow.collect()
                }
                _loginNavItem.emit(LoginNavItem.Login)
            }
        }
    }
}