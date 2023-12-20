package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppearanceDaysRequestDto
import app.threedollars.domain.dto.MenusDto
import kotlinx.coroutines.flow.Flow

interface BossStoreUseCase {
    fun putBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto> = listOf(),
        categoriesIds: List<String> = listOf(),
        imageUrl: String? = null,
        introduction: String? = null,
        menus: List<MenusDto> = listOf(),
        name: String? = null,
        snsUrl: String? = null
    ): Flow<Resource<String>>

    fun patchBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto>? = null,
        categoriesIds: List<String>? = null,
        imageUrl: String? = null,
        introduction: String? = null,
        menus: List<MenusDto>? = null,
        name: String? = null,
        snsUrl: String? = null,
        accountNumber: String? = null,
        accountHolder: String? = null,
        accountBank : String? = null
    ): Flow<Resource<String>>
}