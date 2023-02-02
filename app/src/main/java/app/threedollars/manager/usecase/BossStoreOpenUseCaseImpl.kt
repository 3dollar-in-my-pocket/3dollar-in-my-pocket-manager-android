package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.BossStoreOpenUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BossStoreOpenUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : BossStoreOpenUseCase {
    override fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>> = storeRepository.deleteBossStoreOpen(bossStoreId)

    override fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Resource<String>> =
        storeRepository.postBossStoreOpen(bossStoreId, mapLatitude, mapLongitude)
}