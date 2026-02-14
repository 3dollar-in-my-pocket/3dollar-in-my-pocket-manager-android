package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreReviewDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreReviewDetailUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        reviewId: String,
    ): Flow<Resource<StoreReviewDto.StoreReview>> {
        return reviewRepository.getStoreReviewDetail(
            reviewId = reviewId
        )
    }
}