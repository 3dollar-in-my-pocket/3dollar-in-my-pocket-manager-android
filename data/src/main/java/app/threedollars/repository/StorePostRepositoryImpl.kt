package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.request.StorePostRequest
import app.threedollars.domain.dto.StorePostCreateDto
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostListDto
import app.threedollars.domain.dto.StorePostSectionRequestDto
import app.threedollars.domain.repository.StorePostRepository
import app.threedollars.source.RemoteDataSource
import javax.inject.Inject

internal class StorePostRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : StorePostRepository {

    override suspend fun getStorePosts(storeId: String, size: Int, cursor: String?): Resource<StorePostListDto> =
        runCatching {
            remoteDataSource.getStorePosts(storeId = storeId, size = size, cursor = cursor)
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

    override suspend fun postStorePost(
        storeId: String,
        body: String,
        sections: List<StorePostSectionRequestDto>,
    ): Resource<StorePostCreateDto> = runCatching {
        remoteDataSource.postStorePost(storeId = storeId, request = StorePostRequest.from(body, sections))
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = StorePostCreateDto(postId = it.data!!.postId), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )

    override suspend fun patchStorePost(
        storeId: String,
        postId: String,
        body: String,
        sections: List<StorePostSectionRequestDto>,
    ): Resource<StorePostDto> = runCatching {
        remoteDataSource.patchStorePost(
            storeId = storeId,
            postId = postId,
            request = StorePostRequest.from(body, sections),
        )
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

    override suspend fun deleteStorePost(storeId: String, postId: String): Resource<String> = runCatching {
        remoteDataSource.deleteStorePost(storeId = storeId, postId = postId)
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )
}
