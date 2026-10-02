package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.data.request.StoreMessageCreateRequest
import app.threedollars.domain.dto.StoreMessageCreateDto
import app.threedollars.domain.dto.StoreMessageListDto
import app.threedollars.domain.repository.StoreMessageRepository
import app.threedollars.source.RemoteDataSource
import javax.inject.Inject

internal class StoreMessageRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : StoreMessageRepository {

    override suspend fun getMessages(storeId: String, size: Int, cursor: String?): Resource<StoreMessageListDto> = runCatching {
        remoteDataSource.getStoreMessages(storeId = storeId, size = size, cursor = cursor)
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )

    override suspend fun sendMessage(storeId: String, nonce: String, body: String): Resource<StoreMessageCreateDto> = runCatching {
        remoteDataSource.postStoreMessage(storeId = storeId, nonce = nonce, request = StoreMessageCreateRequest(body = body))
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )
}
