package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.data.request.BossAccountInfoRequest
import app.threedollars.data.request.BossDeviceRequest
import app.threedollars.data.request.LoginRequest
import app.threedollars.data.request.SignUpRequest
import app.threedollars.domain.dto.LoginDto
import app.threedollars.domain.repository.UserRepository
import app.threedollars.source.LocalDataSource
import app.threedollars.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) :
    UserRepository {

    override suspend fun saveAccessToken(token: String) = localDataSource.saveAccessToken(token)

    override fun getAccessToken(): Flow<Resource<String>> = localDataSource.getAccessToken().map {
        Resource.Success(data = it, code = null)
    }

    override fun login(socialType: String, token: String): Flow<Resource<LoginDto>> {
        val loginRequest = LoginRequest(socialType, token)
        return remoteDataSource.login(loginRequest).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        }
    }

    override fun logout(): Flow<Resource<String>> = remoteDataSource.logout()

    override fun signUp(
        bossName: String,
        businessNumber: String,
        certificationPhotoUrl: String,
        socialType: String,
        storeCategoriesIds: List<String>,
        storeName: String,
        token: String
    ): Flow<Resource<LoginDto>> {
        val signUpRequest = SignUpRequest(bossName, businessNumber, certificationPhotoUrl, socialType, storeCategoriesIds, storeName, token)
        return remoteDataSource.signUp(signUpRequest).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        }
    }

    override fun signOut(): Flow<Resource<String>> = remoteDataSource.signOut()

    override fun getBossAccount(): Flow<Resource<app.threedollars.domain.dto.BossAccountInfoDto>> =
        remoteDataSource.getBossAccount().map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        }

    override fun putBossAccount(name: String): Flow<Resource<String>> {
        val bossAccountInfoRequest = BossAccountInfoRequest(name)
        return remoteDataSource.putBossAccount(bossAccountInfoRequest)
    }

    override fun putBossDevice(pushPlatformType: String, pushToken: String): Flow<Resource<String>> {
        val bossDeviceRequest = BossDeviceRequest(pushPlatformType, pushToken)
        return remoteDataSource.putBossDevice(bossDeviceRequest)
    }

    override fun deleteBossDevice(): Flow<Resource<String>> = remoteDataSource.deleteBossDevice()

    override fun putBossDeviceToken(pushPlatformType: String, pushToken: String): Flow<Resource<String>> {
        val bossDeviceRequest = BossDeviceRequest(pushPlatformType, pushToken)
        return remoteDataSource.putBossDeviceToken(bossDeviceRequest)
    }
}