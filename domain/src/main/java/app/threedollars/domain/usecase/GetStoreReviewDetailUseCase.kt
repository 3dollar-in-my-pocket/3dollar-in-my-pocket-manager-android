package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreReviewDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreReviewDetailUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    operator fun invoke(
        storeId: String,
        reviewId: String,
    ): Flow<Resource<StoreReviewDto.StoreReview>> {
        return reviewRepository.getStoreReviewDetail(
            storeId = storeId,
            reviewId = reviewId
        )
    }
}