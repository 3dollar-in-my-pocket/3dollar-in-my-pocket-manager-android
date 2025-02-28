package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CommentPresetDto
import app.threedollars.domain.repository.ReviewRepository
import javax.inject.Inject

class PatchStoreCommentPresetUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        presetId: String,
        body: String
    ): Resource<CommentPresetDto.CommentPreset> {
        return reviewRepository.patchStoreCommentPreset(
            storeId = storeId,
            presetId = presetId,
            body = body
        )
    }
}