package app.threedollars.manager.sign

import app.threedollars.manager.sign.viewmodel.KakaoTokenResult
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class KakaoTokenProvider @Inject constructor() {

    suspend fun refreshAccessToken(): KakaoTokenResult = suspendCancellableCoroutine { continuation ->
        if (!AuthApiClient.instance.hasToken()) {
            continuation.resume(KakaoTokenResult.Expired)
            return@suspendCancellableCoroutine
        }
        AuthApiClient.instance.refreshAccessToken { token, error ->
            if (!continuation.isActive) return@refreshAccessToken
            val result = when {
                error is AuthError && error.reason == AuthErrorCause.InvalidGrant -> KakaoTokenResult.Expired
                error != null -> KakaoTokenResult.Failure
                else -> KakaoTokenResult.of(token?.accessToken)
            }
            continuation.resume(result)
        }
    }
}
