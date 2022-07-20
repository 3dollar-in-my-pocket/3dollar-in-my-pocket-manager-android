package app.threedollars.domain.repository

import app.threedollars.domain.LoginMethod
import app.threedollars.domain.entity.user.NewUser
import app.threedollars.domain.entity.user.User
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    suspend fun loginWithKakao(onResult: (Result<String?>) -> Unit)

    suspend fun saveAccessToken(token: String)

    suspend fun getAccessToken(): Flow<String>

    suspend fun deleteMyAccount(onResult: (Result<Unit>) -> Unit)

    suspend fun logout(onResult: (Result<Unit>) -> Unit)

    suspend fun startSocialLogin(method: LoginMethod, onResult: (Result<Unit>) -> Unit)

    suspend fun loginToManagerServer(
        method: LoginMethod,
        accessToken: String
    ): Result<User>

    suspend fun signUpToManagerServer(
        kakaoAccessToken: String,
        newUserInfo: NewUser
    ): Result<User>
}