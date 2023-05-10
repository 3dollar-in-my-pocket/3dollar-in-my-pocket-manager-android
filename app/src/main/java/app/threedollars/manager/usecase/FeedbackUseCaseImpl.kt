package app.threedollars.manager.usecase

import androidx.paging.PagingData
import app.threedollars.common.Resource
import app.threedollars.domain.dto.ContentsDto
import app.threedollars.domain.dto.FeedbackFullDto
import app.threedollars.domain.dto.FeedbackTypesDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.FeedbackUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedbackUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : FeedbackUseCase {
    override fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullDto>>> =
        storeRepository.getFeedbackFull(targetType, targetId)

    override fun getFeedbackSpecific(targetId: String): Flow<PagingData<ContentsDto>> =
        storeRepository.getFeedbackSpecific(targetId)

    override fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesDto>>> =
        storeRepository.getFeedbackTypes(targetType)
}