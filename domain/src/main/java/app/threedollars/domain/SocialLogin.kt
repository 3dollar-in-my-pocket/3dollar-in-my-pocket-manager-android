package app.threedollars.domain

import app.threedollars.domain.entity.SocialLoginToken

interface SocialLogin {

    fun getToken(onResult: (Result<SocialLoginToken>) -> Unit)

    suspend fun refreshToken(refreshToken: String): SocialLoginToken?

    suspend fun signOut(callback: (error: Throwable?) -> Unit)

    suspend fun logout(callback: (error: Throwable?) -> Unit)
}
