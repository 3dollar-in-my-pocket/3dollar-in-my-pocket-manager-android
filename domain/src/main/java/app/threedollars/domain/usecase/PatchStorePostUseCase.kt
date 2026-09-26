package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostSectionRequestDto
import app.threedollars.domain.repository.StorePostRepository
import javax.inject.Inject

class PatchStorePostUseCase @Inject constructor(
    private val storePostRepository: StorePostRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        postId: String,
        body: String,
        sections: List<StorePostSectionRequestDto>,
    ): Resource<StorePostDto> =
        storePostRepository.patchStorePost(storeId = storeId, postId = postId, body = body, sections = sections)
}
