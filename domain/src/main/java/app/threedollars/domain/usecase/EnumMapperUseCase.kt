package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossEnumsDto
import kotlinx.coroutines.flow.Flow

interface EnumMapperUseCase {
    fun getBossEnums(): Flow<Resource<BossEnumsDto>>
}