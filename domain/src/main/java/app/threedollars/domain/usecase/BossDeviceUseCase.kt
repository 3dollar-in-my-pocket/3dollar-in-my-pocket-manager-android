package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import kotlinx.coroutines.flow.Flow

interface BossDeviceUseCase {
    fun putBossDevice(pushPlatformType: String, pushToken: String): Flow<Resource<String>>

    fun deleteBossDevice(): Flow<Resource<String>>

    fun putBossDeviceToken(pushPlatformType: String, pushToken: String): Flow<Resource<String>>
}