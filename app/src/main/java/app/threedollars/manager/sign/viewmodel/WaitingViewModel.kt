package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.manager.sign.LoginNavItem
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel @Inject constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    private val _loginNavItem = MutableEventFlow<LoginNavItem>()
    val loginNavItem: EventFlow<LoginNavItem> get() = _loginNavItem

    fun logout() {
        viewModelScope.launch(exceptionHandler) {
            authUseCase.logout(fetchFirebaseTokenOrNull()).collect {
                val accessTokenDeferred = async { authUseCase.saveAccessToken("") }
                val socialAccessTokenDeferred = async { authUseCase.saveSocialAccessToken("") }
                awaitAll(accessTokenDeferred, socialAccessTokenDeferred).forEach { flow ->
                    flow.collect()
                }
                _loginNavItem.emit(LoginNavItem.Login)
            }
        }
    }

    private suspend fun fetchFirebaseTokenOrNull(): String? = try {
        withTimeoutOrNull(FIREBASE_TOKEN_TIMEOUT_MILLIS) { FirebaseMessaging.getInstance().token.await() }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null
    }

    private companion object {
        const val FIREBASE_TOKEN_TIMEOUT_MILLIS = 3_000L
    }
}