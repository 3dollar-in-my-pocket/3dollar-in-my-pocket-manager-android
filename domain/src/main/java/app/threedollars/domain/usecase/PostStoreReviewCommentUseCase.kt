package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CommentCreateDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostStoreReviewCommentUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        reviewId: String,
        reviewComment: String,
    ): Resource<CommentCreateDto> {
        return reviewRepository.postStoreReviewComment(
            storeId = storeId,
            reviewId = reviewId,
            reviewComment = reviewComment,
        )
    }
}