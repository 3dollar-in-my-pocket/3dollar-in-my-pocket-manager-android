package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CommentPresetDto
import app.threedollars.domain.repository.ReviewRepository
import javax.inject.Inject

class GetStoreCommentPresetListUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
    ): Resource<CommentPresetDto> {
        return reviewRepository.getStoreCommentPresets(
            storeId = storeId,
        )
    }
}