package app.threedollars.repository

import app.threedollars.Resource
import app.threedollars.data.request.LoginRequest
import app.threedollars.dto.BossAccountInfoDto
import app.threedollars.dto.LoginDto
import app.threedollars.source.RemoteDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) : UserRepository {
    override fun login(socialType: String, token: String): Resource<LoginDto> {
        val loginRequest = LoginRequest(socialType, token)
    }

    override fun logout(): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun signUp(
        bossName: String,
        businessNumber: String,
        certificationPhotoUrl: String,
        socialType: String,
        storeCategoriesIds: List<String>,
        storeName: String,
        token: String
    ): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun signOut(): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun getBossAccount(): Resource<BossAccountInfoDto> {
        TODO("Not yet implemented")
    }

    override fun putBossAccount(name: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun putBossDevice(pushPlatformType: String, pushToken: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun deleteBossDevice(): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun putBossDeviceToken(pushPlatformType: String, pushToken: String): Resource<String> {
        TODO("Not yet implemented")
    }
}