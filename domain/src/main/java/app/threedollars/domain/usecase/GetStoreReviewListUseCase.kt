package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreReviewDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreReviewListUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    operator fun invoke(
        storeId: String,
        sort: String,
    ): Flow<Resource<StoreReviewDto>> {
        return reviewRepository.getStoreReviews(
            storeId = storeId,
            sort = sort,
        )
    }
}