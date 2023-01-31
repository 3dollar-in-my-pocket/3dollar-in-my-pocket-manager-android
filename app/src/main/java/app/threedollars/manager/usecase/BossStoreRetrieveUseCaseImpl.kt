package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossStoreRetrieveAroundDto
import app.threedollars.domain.dto.BossStoreRetrieveDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BossStoreRetrieveUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : BossStoreRetrieveUseCase {
    override fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Flow<Resource<BossStoreRetrieveDto>> =
        storeRepository.getBossStoreRetrieveSpecific(bossStoreId, latitude, longitude)

    override fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveDto>> = storeRepository.getBossStoreRetrieveMe()

    override fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        latitude: Double,
        longitude: Double,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int
    ): Flow<Resource<List<BossStoreRetrieveAroundDto>>> =
        storeRepository.getBossStoreRetrieveAround(categoryId, distanceKm, latitude, longitude, mapLatitude, mapLongitude, orderType, size)
}