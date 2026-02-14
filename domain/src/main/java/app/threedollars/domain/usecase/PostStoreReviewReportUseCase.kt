package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostStoreReviewReportUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    operator fun invoke(
        storeId: String,
        reviewId: String,
        reasonDetail: String,
    ): Flow<Resource<String>> {
        return reviewRepository.postStoreReviewReport(
            storeId = storeId,
            reviewId = reviewId,
            reasonDetail = reasonDetail,
        )
    }
}