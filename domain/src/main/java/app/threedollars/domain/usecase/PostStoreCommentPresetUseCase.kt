package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CommentPresetDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostStoreCommentPresetUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        body: String,
    ): Resource<CommentPresetDto.CommentPreset> {
        return reviewRepository.postStoreCommentPreset(
            storeId = storeId,
            body = body,
        )
    }
}