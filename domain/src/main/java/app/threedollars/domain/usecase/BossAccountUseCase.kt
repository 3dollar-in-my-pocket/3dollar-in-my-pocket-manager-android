package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossAccountInfoDto
import kotlinx.coroutines.flow.Flow

interface BossAccountUseCase {
    fun getBossAccount(): Flow<Resource<BossAccountInfoDto>>

    fun putBossAccount(name: String): Flow<Resource<String>>
}