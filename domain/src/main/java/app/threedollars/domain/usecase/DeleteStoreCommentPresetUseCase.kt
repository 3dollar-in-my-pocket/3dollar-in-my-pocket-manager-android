package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteStoreCommentPresetUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        presetId: String,
    ): Resource<String> {
        return reviewRepository.deleteStoreCommentPreset(
            storeId = storeId,
            presetId = presetId,
        )
    }
}