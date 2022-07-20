package app.threedollars.data.datastore

import app.threedollars.domain.LoginMethod
import app.threedollars.domain.entity.user.NewUser
import app.threedollars.domain.entity.user.User
import app.threedollars.domain.repository.SignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val localSignDataSource: LocalSignDataSourceImpl,
    private val remoteSignDataSource: RemoteSignDataSourceImpl,
) : SignRepository {

    override suspend fun loginWithKakao(onResult: (Result<String?>) -> Unit) {
        localSignDataSource.loginWithKakao(onResult)
        remoteSignDataSource.loginWithKakao(onResult)
    }

    override suspend fun saveAccessToken(token: String) {
        localSignDataSource.saveAccessToken(token)
    }

    override suspend fun getAccessToken(): Flow<String> {
        return remoteSignDataSource.getAccessToken()
    }

    override suspend fun deleteMyAccount(onResult: (Result<Unit>) -> Unit) {
        remoteSignDataSource.deleteMyAccount(onResult)
    }

    override suspend fun logout(onResult: (Result<Unit>) -> Unit) {
        remoteSignDataSource.logout(onResult)
    }

    override suspend fun startSocialLogin(method: LoginMethod, onResult: (Result<Unit>) -> Unit) {
        remoteSignDataSource.startSocialLogin(method, onResult)
    }

    override suspend fun loginToManagerServer(
        method: LoginMethod,
        accessToken: String
    ): Result<User> {
        return remoteSignDataSource.loginToManagerServer(method, accessToken)
    }

    override suspend fun signUpToManagerServer(
        kakaoAccessToken: String,
        newUserInfo: NewUser
    ): Result<User> {
        return remoteSignDataSource.signUpToManagerServer(kakaoAccessToken, newUserInfo)
    }
}