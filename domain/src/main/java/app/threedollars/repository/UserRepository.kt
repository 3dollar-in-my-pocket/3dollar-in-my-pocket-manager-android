package app.threedollars.repository

import app.threedollars.Resource
import app.threedollars.dto.BossAccountInfoDto
import app.threedollars.dto.LoginDto

interface UserRepository {

    fun login(socialType: String, token: String): Resource<LoginDto>

    fun logout(): Resource<String>

    fun signUp(
        bossName: String,
        businessNumber: String,
        certificationPhotoUrl: String,
        socialType: String,
        storeCategoriesIds: List<String>,
        storeName: String,
        token: String
    ): Resource<String>

    fun signOut(): Resource<String>

    // boss-account-controller
    fun getBossAccount(): Resource<BossAccountInfoDto>

    fun putBossAccount(name: String): Resource<String>

    // boss-device-controller
    fun putBossDevice(pushPlatformType: String, pushToken: String): Resource<String>

    fun deleteBossDevice(): Resource<String>

    fun putBossDeviceToken(pushPlatformType: String, pushToken: String): Resource<String>

}