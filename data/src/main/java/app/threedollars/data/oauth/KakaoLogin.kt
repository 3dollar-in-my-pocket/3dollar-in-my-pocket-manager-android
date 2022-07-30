package app.threedollars.data.oauth

import android.content.Context
import app.threedollars.data.BuildConfig
import app.threedollars.data.user.KakaoLoginApi
import app.threedollars.domain.LoginMethod
import app.threedollars.domain.SocialLogin
import app.threedollars.domain.entity.SocialLoginToken
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class KakaoLogin @Inject constructor(
    private val kakaoLoginApi: KakaoLoginApi,
    @ApplicationContext private val context: Context,
) : SocialLogin {

    override fun getToken(
        onResult: (Result<SocialLoginToken>) -> Unit
    ) {
        val loginResCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                onResult(Result.failure(error))
            } else if (token != null) {
                onResult(
                    Result.success(
                        SocialLoginToken(
                            token.accessToken,
                            token.refreshToken,
                            LoginMethod.KAKAO
                        )
                    )
                )
            }
        }

        if (LoginClient.instance.isKakaoTalkLoginAvailable(context)) {
            LoginClient.instance.loginWithKakaoTalk(
                context = context,
                callback = loginResCallback
            )
        } else {
            LoginClient.instance.loginWithKakaoAccount(
                context = context,
                callback = loginResCallback
            )
        }
    }

    override suspend fun refreshToken(refreshToken: String): SocialLoginToken? {
        val data = kakaoLoginApi.refreshToken(
            refreshToken,
            BuildConfig.KAKAO_AUTH_KEY,
            "refresh_token"
        )

        return if (data.isSuccessful) SocialLoginToken(
            data.body()?.accessToken ?: "",
            data.body()?.refreshToken ?: "",
            LoginMethod.KAKAO
        ) else {
            null
        }
    }

    override suspend fun signOut(callback: (error: Throwable?) -> Unit) {
        UserApiClient.instance.unlink(callback)
    }

    override suspend fun logout(callback: (error: Throwable?) -> Unit) {
        UserApiClient.instance.logout(callback)
    }
}