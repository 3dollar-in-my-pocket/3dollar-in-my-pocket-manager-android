package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossEnumsDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.EnumMapperUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EnumMapperUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : EnumMapperUseCase{
    override fun getBossEnums(): Flow<Resource<BossEnumsDto>> = storeRepository.getBossEnums()
}