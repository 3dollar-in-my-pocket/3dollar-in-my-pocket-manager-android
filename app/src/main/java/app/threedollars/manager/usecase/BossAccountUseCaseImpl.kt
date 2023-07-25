package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossAccountInfoDto
import app.threedollars.domain.repository.UserRepository
import app.threedollars.domain.usecase.BossAccountUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BossAccountUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : BossAccountUseCase {
    override fun getBossAccount(): Flow<Resource<BossAccountInfoDto>> = userRepository.getBossAccount()

    override fun putBossAccount(name: String): Flow<Resource<String>> = userRepository.putBossAccount(name)
}