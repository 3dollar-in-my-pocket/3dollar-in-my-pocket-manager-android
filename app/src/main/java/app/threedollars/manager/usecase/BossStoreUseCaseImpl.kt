package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppearanceDaysRequestDto
import app.threedollars.domain.dto.MenusDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.BossStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BossStoreUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : BossStoreUseCase {
    override fun putBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto>,
        categoriesIds: List<String>,
        imageUrl: String?,
        introduction: String?,
        menus: List<MenusDto>,
        name: String?,
        snsUrl: String?
    ): Flow<Resource<String>> =
        storeRepository.putBossStore(bossStoreId, appearanceDays, categoriesIds, imageUrl, introduction, menus, name, snsUrl)

    override fun patchBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto>?,
        categoriesIds: List<String>?,
        imageUrl: String?,
        introduction: String?,
        menus: List<MenusDto>?,
        name: String?,
        snsUrl: String?
    ): Flow<Resource<String>> =
        storeRepository.patchBossStore(bossStoreId, appearanceDays, categoriesIds, imageUrl, introduction, menus, name, snsUrl)
}