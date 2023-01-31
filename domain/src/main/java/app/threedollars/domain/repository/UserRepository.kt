package app.threedollars.domain.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossAccountInfoDto
import app.threedollars.domain.dto.LoginDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun login(socialType: String, token: String): Flow<Resource<LoginDto>>

    fun logout(): Flow<Resource<String>>

    fun signUp(
        bossName: String,
        businessNumber: String,
        certificationPhotoUrl: String,
        socialType: String,
        storeCategoriesIds: List<String>,
        storeName: String,
        token: String
    ): Flow<Resource<String>>

    fun signOut(): Flow<Resource<String>>

    // boss-account-controller
    fun getBossAccount(): Flow<Resource<BossAccountInfoDto>>

    fun putBossAccount(name: String): Flow<Resource<String>>

    // boss-device-controller
    fun putBossDevice(pushPlatformType: String, pushToken: String): Flow<Resource<String>>

    fun deleteBossDevice(): Flow<Resource<String>>

    fun putBossDeviceToken(pushPlatformType: String, pushToken: String): Flow<Resource<String>>

}