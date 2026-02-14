package app.threedollars.domain.usecase

import androidx.paging.PagingData
import app.threedollars.common.Resource
import app.threedollars.domain.dto.ContentsDto
import app.threedollars.domain.dto.StoreReviewDto
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreReviewPagingUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    operator fun invoke(
        storeId: String,
        sort: String,
    ): Flow<PagingData<StoreReviewDto.StoreReview>> {
        return reviewRepository.getStoreReviewPaging(
            storeId = storeId,
            sort = sort,
        )
    }
}