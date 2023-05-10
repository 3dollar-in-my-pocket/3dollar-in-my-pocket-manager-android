package app.threedollars.domain.usecase

import androidx.paging.PagingData
import app.threedollars.common.Resource
import app.threedollars.domain.dto.ContentsDto
import app.threedollars.domain.dto.FeedbackFullDto
import app.threedollars.domain.dto.FeedbackSpecificDto
import app.threedollars.domain.dto.FeedbackTypesDto
import kotlinx.coroutines.flow.Flow

interface FeedbackUseCase {
    fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullDto>>>

    fun getFeedbackSpecific(targetId: String): Flow<PagingData<ContentsDto>>

    fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesDto>>>

}