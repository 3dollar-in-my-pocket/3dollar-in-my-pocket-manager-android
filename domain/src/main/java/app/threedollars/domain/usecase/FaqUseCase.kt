package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.FaqCategoriesDto
import app.threedollars.domain.dto.FaqDto
import kotlinx.coroutines.flow.Flow

interface FaqUseCase {
    fun getFaqCategories(): Flow<Resource<List<FaqCategoriesDto>>>

    fun getFaqs(category: String = ""): Flow<Resource<List<FaqDto>>>
}