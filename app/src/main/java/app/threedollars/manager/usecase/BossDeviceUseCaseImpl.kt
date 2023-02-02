package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.UserRepository
import app.threedollars.domain.usecase.BossDeviceUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BossDeviceUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : BossDeviceUseCase {
    override fun putBossDevice(pushPlatformType: String, pushToken: String): Flow<Resource<String>> =
        userRepository.putBossDevice(pushPlatformType, pushToken)

    override fun deleteBossDevice(): Flow<Resource<String>> = userRepository.deleteBossDevice()

    override fun putBossDeviceToken(pushPlatformType: String, pushToken: String): Flow<Resource<String>> =
        userRepository.putBossDeviceToken(pushPlatformType, pushToken)
}