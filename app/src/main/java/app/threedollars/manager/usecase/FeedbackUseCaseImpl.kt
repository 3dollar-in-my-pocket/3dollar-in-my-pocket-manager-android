package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.FeedbackFullDto
import app.threedollars.domain.dto.FeedbackSpecificDto
import app.threedollars.domain.dto.FeedbackTypesDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.FeedbackUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedbackUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : FeedbackUseCase {
    override fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullDto>>> =
        storeRepository.getFeedbackFull(targetType, targetId)

    override fun getFeedbackSpecific(targetType: String, targetId: String, startDAte: String, endDate: String): Flow<Resource<FeedbackSpecificDto>> =
        storeRepository.getFeedbackSpecific(targetType, targetId, startDAte, endDate)

    override fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesDto>>> =
        storeRepository.getFeedbackTypes(targetType)
}