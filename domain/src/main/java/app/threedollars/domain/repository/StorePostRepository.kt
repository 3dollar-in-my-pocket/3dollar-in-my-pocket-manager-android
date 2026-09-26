package app.threedollars.domain.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePostCreateDto
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostListDto
import app.threedollars.domain.dto.StorePostSectionRequestDto

interface StorePostRepository {
    suspend fun getStorePosts(storeId: String, size: Int, cursor: String?): Resource<StorePostListDto>

    suspend fun postStorePost(
        storeId: String,
        body: String,
        sections: List<StorePostSectionRequestDto>,
    ): Resource<StorePostCreateDto>

    suspend fun patchStorePost(
        storeId: String,
        postId: String,
        body: String,
        sections: List<StorePostSectionRequestDto>,
    ): Resource<StorePostDto>

    suspend fun deleteStorePost(storeId: String, postId: String): Resource<String>
}
