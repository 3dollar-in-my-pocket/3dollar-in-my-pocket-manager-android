package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePostCreateDto
import app.threedollars.domain.dto.StorePostSectionRequestDto
import app.threedollars.domain.repository.StorePostRepository
import javax.inject.Inject

class PostStorePostUseCase @Inject constructor(
    private val storePostRepository: StorePostRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        body: String,
        sections: List<StorePostSectionRequestDto>,
    ): Resource<StorePostCreateDto> =
        storePostRepository.postStorePost(storeId = storeId, body = body, sections = sections)
}
