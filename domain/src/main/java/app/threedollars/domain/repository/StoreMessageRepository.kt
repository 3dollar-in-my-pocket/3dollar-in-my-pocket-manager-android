package app.threedollars.domain.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StoreMessageCreateDto
import app.threedollars.domain.dto.StoreMessageListDto

interface StoreMessageRepository {
    suspend fun getMessages(storeId: String, size: Int, cursor: String?): Resource<StoreMessageListDto>

    suspend fun sendMessage(storeId: String, nonce: String, body: String): Resource<StoreMessageCreateDto>
}
