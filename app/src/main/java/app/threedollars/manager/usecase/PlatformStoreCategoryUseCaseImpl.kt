package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreCategoriesDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.PlatformStoreCategoryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlatformStoreCategoryUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : PlatformStoreCategoryUseCase {
    override fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesDto>>> = storeRepository.getStoreCategories(storeType)
}