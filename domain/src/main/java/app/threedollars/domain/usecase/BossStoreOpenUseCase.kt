package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import kotlinx.coroutines.flow.Flow

interface BossStoreOpenUseCase {
    fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>>

    fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Resource<String>>
}