package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.ReviewRepository
import javax.inject.Inject

class PutStickersReplaceUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        reviewId: String,
        stickers: String,
    ): Resource<String> {
        return reviewRepository.putStickersReplace(
            storeId = storeId,
            reviewId = reviewId,
            stickers = stickers
        )
    }
}