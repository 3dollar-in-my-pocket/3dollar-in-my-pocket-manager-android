package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreMessageListDto
import app.threedollars.domain.repository.StoreMessageRepository
import javax.inject.Inject

class GetStoreMessageListUseCase @Inject constructor(
    private val storeMessageRepository: StoreMessageRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        size: Int = 20,
        cursor: String? = null,
    ): Resource<StoreMessageListDto> = storeMessageRepository.getMessages(storeId = storeId, size = size, cursor = cursor)
}
