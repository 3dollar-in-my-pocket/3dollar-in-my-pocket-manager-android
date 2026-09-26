package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePostListDto
import app.threedollars.domain.repository.StorePostRepository
import javax.inject.Inject

class GetStorePostListUseCase @Inject constructor(
    private val storePostRepository: StorePostRepository,
) {
    suspend operator fun invoke(storeId: String, size: Int = 20, cursor: String? = null): Resource<StorePostListDto> =
        storePostRepository.getStorePosts(storeId = storeId, size = size, cursor = cursor)
}
