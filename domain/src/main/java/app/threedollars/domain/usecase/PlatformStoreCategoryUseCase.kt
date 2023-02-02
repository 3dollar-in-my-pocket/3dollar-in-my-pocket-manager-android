package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreCategoriesDto
import kotlinx.coroutines.flow.Flow

interface PlatformStoreCategoryUseCase {
    fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesDto>>>
}