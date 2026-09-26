package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.StorePostRepository
import javax.inject.Inject

class DeleteStorePostUseCase @Inject constructor(
    private val storePostRepository: StorePostRepository,
) {
    suspend operator fun invoke(storeId: String, postId: String): Resource<String> =
        storePostRepository.deleteStorePost(storeId = storeId, postId = postId)
}
