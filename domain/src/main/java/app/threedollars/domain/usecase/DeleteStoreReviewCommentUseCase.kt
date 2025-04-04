package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CommentCreateDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteStoreReviewCommentUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        reviewId: String,
        commentId: String,
    ): Resource<String> {
        return reviewRepository.deleteStoreReviewComment(
            storeId = storeId,
            reviewId = reviewId,
            commentId = commentId,
        )
    }
}