package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreMessageCreateDto
import app.threedollars.domain.repository.StoreMessageRepository
import javax.inject.Inject

class SendStoreMessageUseCase @Inject constructor(
    private val storeMessageRepository: StoreMessageRepository,
) {
    suspend operator fun invoke(storeId: String, nonce: String, body: String): Resource<StoreMessageCreateDto> =
        storeMessageRepository.sendMessage(storeId = storeId, nonce = nonce, body = body)
}
