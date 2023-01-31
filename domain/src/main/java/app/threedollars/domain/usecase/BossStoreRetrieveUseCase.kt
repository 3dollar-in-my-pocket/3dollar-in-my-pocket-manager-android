package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossStoreRetrieveAroundDto
import app.threedollars.domain.dto.BossStoreRetrieveDto
import kotlinx.coroutines.flow.Flow

interface BossStoreRetrieveUseCase {
    fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Flow<Resource<BossStoreRetrieveDto>>

    fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveDto>>

    fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        latitude: Double,
        longitude: Double,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int
    ): Flow<Resource<List<BossStoreRetrieveAroundDto>>>

}