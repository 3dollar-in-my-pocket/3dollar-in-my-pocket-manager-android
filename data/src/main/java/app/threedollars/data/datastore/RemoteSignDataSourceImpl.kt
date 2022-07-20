package app.threedollars.data.datastore

import android.content.Context
import app.threedollars.data.datastore.LocalSignDataSourceImpl.Companion.REFRESH_TOKEN
import app.threedollars.data.db.DataStoreHelper
import app.threedollars.data.mapper.UserMapper
import app.threedollars.data.oauth.KakaoLogin
import app.threedollars.data.user.UserService
import app.threedollars.data.user.request.LoginRequest
import app.threedollars.data.user.request.SignUpRequest
import app.threedollars.domain.LoginMethod
import app.threedollars.domain.entity.user.NewUser
import app.threedollars.domain.entity.user.User
import app.threedollars.domain.repository.SignRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject

class RemoteSignDataSourceImpl @Inject constructor(
    private val service: UserService,
    private val socialLogin: KakaoLogin,
    private val dataStoreHelper: DataStoreHelper,
    @ApplicationContext private val context: Context
) : SignRepository {

    override suspend fun startSocialLogin(
        method: LoginMethod,
        onResult: (Result<Unit>) -> Unit
    ) {
        when (method) {
            LoginMethod.KAKAO -> {
                socialLogin.getToken { result ->
                    when {
                        result.isSuccess -> {
                            onResult(Result.success(Unit))
                        }
                        result.isFailure -> result.exceptionOrNull()?.let {
                            Result.failure<Throwable>(it)
                        }
                    }
                }
            }
        }
    }

    override suspend fun loginWithKakao(
        onResult: (Result<String?>) -> Unit
    ) {
        val refreshToken = dataStoreHelper.getStringData(REFRESH_TOKEN).lastOrNull()
        if (refreshToken.isNullOrBlank()) {
            onResult(Result.failure(Exception("invalid token")))
            return
        }

        val refreshResult = socialLogin.refreshToken(refreshToken)
        if (refreshResult.accessToken.isNotBlank()) {
            dataStoreHelper.saveStringData(REFRESH_TOKEN, refreshResult.refreshToken)
            val loginResult = service.login(
                LoginRequest("KAKAO", refreshResult.accessToken)
            )
            when {
                loginResult.code() in 200..299 -> onResult(Result.success(loginResult.body()?.data?.token))
                loginResult.code() in 400..499 -> onResult(Result.failure(Exception("not exist")))
                else -> onResult(Result.failure(Exception("server error")))
            }
        }
    }

    override suspend fun saveAccessToken(token: String) {

    }

    override suspend fun getAccessToken(): Flow<String> {
        return flow { "" }
    }

    override suspend fun loginToManagerServer(
        method: LoginMethod,
        accessToken: String
    ): Result<User> {
        val response = service.login(
            LoginRequest(
                method.name,
                accessToken
            )
        )

        return if (response.isSuccessful) {
            Result.success(UserMapper.loginUserMapper(response.body()?.data))
        } else {
            Result.failure(Throwable(response.errorBody().toString()))
        }
    }

    override suspend fun signUpToManagerServer(
        kakaoAccessToken: String,
        newUserInfo: NewUser,
    ): Result<User> {
        val response = service.signUp(
            SignUpRequest(
                newUserInfo.bossName,
                newUserInfo.businessNumber,
                newUserInfo.certificationPhotoUrl,
                newUserInfo.contactsNumber,
                newUserInfo.socialType,
                newUserInfo.storeCategoriesIds,
                newUserInfo.storeName,
                kakaoAccessToken
            )
        )

        return if (response.isSuccessful) {
            Result.success(UserMapper.loginUserMapper(response.body()?.data))
        } else {
            Result.failure(Throwable(response.errorBody().toString()))
        }
    }

    override suspend fun logout(onResult: (Result<Unit>) -> Unit) {
        socialLogin.logout {
            if (it == null) onResult(Result.success(Unit)) else onResult(Result.failure(it))
        }
        service.logout()
    }

    override suspend fun deleteMyAccount(onResult: (Result<Unit>) -> Unit) {
        socialLogin.signOut {
            if (it == null) onResult(Result.success(Unit)) else onResult(Result.failure(it))
        }
        service.signOut()
    }
}