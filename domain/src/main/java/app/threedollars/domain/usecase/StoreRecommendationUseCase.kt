package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreRecommendationDto
import app.threedollars.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRecommendationUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    fun getStoreRecommendation(storeId: String, date: String): Flow<Resource<StoreRecommendationDto>> =
        storeRepository.getStoreRecommendation(storeId, date)
}
