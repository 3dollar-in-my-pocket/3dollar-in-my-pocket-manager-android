package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.BossStoreRetrieveAroundDto
import app.threedollars.domain.dto.BossStoreRetrieveDto
import kotlinx.coroutines.flow.Flow

interface BossStoreRetrieveUseCase {
    fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Flow<Resource<BossStoreRetrieveDto>>

    fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveDto>>

    fun getBossStoreRetrieveAround(
        categoryId: String = "",
        distanceKm: Int = 1,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String = "DISTANCE_ASC",
        size: Int = 30
    ): Flow<Resource<List<BossStoreRetrieveAroundDto>>>

}