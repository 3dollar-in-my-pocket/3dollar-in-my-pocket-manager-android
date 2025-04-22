package app.threedollars.domain.repository

import androidx.paging.PagingData
import app.threedollars.common.Resource
import app.threedollars.domain.dto.CommentCreateDto
import app.threedollars.domain.dto.CommentPresetDto
import app.threedollars.domain.dto.StoreReviewDto
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getStoreReviews(
        storeId: String,
        sort: String,
    ): Flow<Resource<StoreReviewDto>>

    fun getStoreReviewPaging(
        storeId: String,
        sort: String
    ): Flow<PagingData<StoreReviewDto.StoreReview>>

    suspend fun getStoreReviewDetail(reviewId: String): Flow<Resource<StoreReviewDto.StoreReview>>

    fun postStoreReviewReport(
        storeId: String,
        reviewId: String,
        reasonDetail: String
    ): Flow<Resource<String>>

    suspend fun postStoreReviewComment(
        storeId: String,
        reviewId: String,
        reviewComment: String
    ): Resource<CommentCreateDto>

    suspend fun deleteStoreReviewComment(
        storeId: String,
        reviewId: String,
        commentId: String
    ): Resource<String>

    suspend fun postStoreCommentPreset(
        storeId: String,
        body: String
    ): Resource<CommentPresetDto.CommentPreset>

    suspend fun deleteStoreCommentPreset(
        storeId: String,
        presetId: String,
    ): Resource<String>

    suspend fun patchStoreCommentPreset(
        storeId: String,
        presetId: String,
        body: String
    ): Resource<String>

    suspend fun getStoreCommentPresets(
        storeId: String,
    ): Resource<CommentPresetDto>
}