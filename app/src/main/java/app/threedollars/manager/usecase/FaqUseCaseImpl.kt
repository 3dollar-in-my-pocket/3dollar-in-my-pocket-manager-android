package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.FaqCategoriesDto
import app.threedollars.domain.dto.FaqDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.FaqUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FaqUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : FaqUseCase {
    override fun getFaqCategories(): Flow<Resource<List<FaqCategoriesDto>>> = storeRepository.getFaqCategories()

    override fun getFaqs(category: String): Flow<Resource<List<FaqDto>>> = storeRepository.getFaqs(category)
}