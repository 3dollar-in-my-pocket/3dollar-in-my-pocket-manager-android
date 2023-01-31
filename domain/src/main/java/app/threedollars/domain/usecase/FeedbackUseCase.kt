package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.FeedbackFullDto
import app.threedollars.domain.dto.FeedbackSpecificDto
import app.threedollars.domain.dto.FeedbackTypesDto
import kotlinx.coroutines.flow.Flow

interface FeedbackUseCase {
    fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullDto>>>

    fun getFeedbackSpecific(
        targetType: String,
        targetId: String,
        startDAte: String,
        endDate: String
    ): Flow<Resource<FeedbackSpecificDto>>

    fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesDto>>>

}