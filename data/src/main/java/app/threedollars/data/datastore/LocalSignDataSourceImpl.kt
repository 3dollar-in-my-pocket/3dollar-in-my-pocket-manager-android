package app.threedollars.data.datastore

import app.threedollars.data.db.DataStoreHelper
import app.threedollars.domain.LoginMethod
import app.threedollars.domain.entity.user.NewUser
import app.threedollars.domain.entity.user.User
import app.threedollars.domain.repository.SignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSignDataSourceImpl @Inject constructor(
    private val dataStoreHelper: DataStoreHelper
) : SignRepository {

    override suspend fun loginWithKakao(token: String, onResult: (Result<String?>) -> Unit) {
        dataStoreHelper.saveStringData(KAKAO_ACCESS_TOKEN, token)
    }

    override suspend fun saveAccessToken(token: String) {
        dataStoreHelper.saveStringData(ACCESS_TOKEN, token)
    }

    override suspend fun getAccessToken(): Flow<String> {
        return dataStoreHelper.getStringData(ACCESS_TOKEN)
    }

    override suspend fun saveKakaoAccessToken(token: String) {
        dataStoreHelper.saveStringData(KAKAO_ACCESS_TOKEN, token)
    }

    override suspend fun getKakaoAccessToken(): Flow<String> {
        return dataStoreHelper.getStringData(KAKAO_ACCESS_TOKEN)
    }

    override suspend fun saveKakaoRefreshToken(token: String) {
        dataStoreHelper.saveStringData(KAKAO_REFRESH_TOKEN, token)
    }

    override suspend fun getKakaoRefreshToken(): Flow<String> {
        return dataStoreHelper.getStringData(KAKAO_REFRESH_TOKEN)
    }

    override suspend fun deleteMyAccount(onResult: (Result<Unit>) -> Unit) {
        dataStoreHelper.saveStringData(ACCESS_TOKEN, "")
        dataStoreHelper.saveStringData(REFRESH_TOKEN, "")
        dataStoreHelper.saveStringData(SOCIAL_ACCESS_TOKEN, "")
        dataStoreHelper.saveStringData(SOCIAL_REFRESH_TOKEN, "")
    }

    override suspend fun logout(onResult: (Result<Unit>) -> Unit) {
        dataStoreHelper.saveStringData(ACCESS_TOKEN, "")
        dataStoreHelper.saveStringData(REFRESH_TOKEN, "")
        dataStoreHelper.saveStringData(SOCIAL_ACCESS_TOKEN, "")
        dataStoreHelper.saveStringData(SOCIAL_REFRESH_TOKEN, "")
    }

    override suspend fun startSocialLogin(method: LoginMethod, onResult: (Result<Unit>) -> Unit) {

    }

    override suspend fun loginToManagerServer(
        method: LoginMethod,
        accessToken: String
    ): Result<User> {
        return Result.success(User("", ""))
    }

    override suspend fun signUpToManagerServer(
        kakaoAccessToken: String,
        newUserInfo: NewUser
    ): Result<User> {
        return Result.success(User("", ""))
    }

    companion object {
        val ACCESS_TOKEN = "access_token"
        val KAKAO_ACCESS_TOKEN = "kakao_access_token"
        val REFRESH_TOKEN = "refresh_token"
        val KAKAO_REFRESH_TOKEN = "kakao_refresh_token"
        val SOCIAL_ACCESS_TOKEN = "social_access_token"
        val SOCIAL_REFRESH_TOKEN = "social_refresh_token"
        val LOGIN_METHOD = "login_method"
    }
}