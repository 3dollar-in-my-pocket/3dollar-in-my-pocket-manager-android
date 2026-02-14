package app.threedollars.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.threedollars.common.Resource
import app.threedollars.common.ext.toStringDefault
import app.threedollars.domain.dto.CommentCreateDto
import app.threedollars.domain.dto.CommentPresetDto
import app.threedollars.domain.dto.StoreReviewDto
import app.threedollars.domain.repository.ReviewRepository
import app.threedollars.network.NetworkService
import app.threedollars.source.RemoteDataSource
import app.threedollars.source.ReviewDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ReviewRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val networkService: NetworkService,
) : ReviewRepository {
    override fun getStoreReviews(
        storeId: String,
        sort: String,
    ): Flow<Resource<StoreReviewDto>> = remoteDataSource.getStoreReviews(
        storeId = storeId,
        sort = sort,
    ).map {
        if (it.data != null) {
            Resource.Success(data = it.data!!.toDto(), code = it.code)
        } else {
            Resource.Error(errorMessage = it.errorMessage, code = it.code)
        }
    }

    override fun getStoreReviewPaging(
        storeId: String,
        sort: String,
    ): Flow<PagingData<StoreReviewDto.StoreReview>> = Pager(PagingConfig(pageSize = 10)) {
        ReviewDataSource(
            networkService = networkService,
            storeId = storeId,
            sort = sort
        )
    }.flow.map {
        it.map { storeReview ->
            storeReview.toDto()
        }
    }

    override suspend fun getStoreReviewDetail(
        reviewId: String,
    ): Flow<Resource<StoreReviewDto.StoreReview>> {
        val storeId =
            remoteDataSource.getBossStoreRetrieveMe().first().data?.bossStoreId.toStringDefault()
        return remoteDataSource.getStoreReviewDetail(
            storeId = storeId,
            reviewId = reviewId
        ).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        }
    }

    override fun postStoreReviewReport(
        storeId: String,
        reviewId: String,
        reasonDetail: String,
    ): Flow<Resource<String>> {
        return remoteDataSource.postStoreReviewReport(
            storeId = storeId,
            reviewId = reviewId,
            reasonDetail = reasonDetail
        ).map {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        }
    }

    override suspend fun postStoreReviewComment(
        storeId: String,
        reviewId: String,
        reviewComment: String,
    ): Resource<CommentCreateDto> = runCatching {
        remoteDataSource.postStoreReviewComment(
            storeId = storeId,
            reviewId = reviewId,
            nonce = postNonce(),
            reviewComment = reviewComment
        )
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = {
            Resource.Error(errorMessage = it.message, code = "700")
        }
    )

    override suspend fun putStickersReplace(
        storeId: String, reviewId: String,
        stickers: String,
    ): Resource<String> = runCatching {
        remoteDataSource.putStickersReplace(
            storeId = storeId,
            reviewId = reviewId,
            stickers = stickers
        )
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!, code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = {
            Resource.Error(errorMessage = it.message, code = "")
        }
    )

    override suspend fun deleteStoreReviewComment(
        storeId: String,
        reviewId: String,
        commentId: String,
    ): Resource<String> = runCatching {
        remoteDataSource.deleteStoreReviewComment(
            storeId = storeId,
            reviewId = reviewId,
            commentId = commentId
        )
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = {
            Resource.Error(errorMessage = it.message, code = "700")
        }
    )

    override suspend fun postStoreCommentPreset(
        storeId: String,
        body: String,
    ): Resource<CommentPresetDto.CommentPreset> = runCatching {
        remoteDataSource.postStoreCommentPreset(
            storeId = storeId,
            nonce = postNonce(),
            body = body
        )
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = {
            Resource.Error(errorMessage = it.message, code = "700")
        }
    )

    override suspend fun deleteStoreCommentPreset(
        storeId: String,
        presetId: String,
    ): Resource<String> = runCatching {
        remoteDataSource.deleteStoreCommentPreset(
            storeId = storeId,
            presetId = presetId
        )
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = {
            Resource.Error(errorMessage = it.message, code = "700")
        }
    )

    override suspend fun patchStoreCommentPreset(
        storeId: String,
        presetId: String,
        body: String,
    ): Resource<String> = runCatching {
        remoteDataSource.patchStoreCommentPreset(
            storeId = storeId,
            presetId = presetId,
            body = body
        )
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = {
            Resource.Error(errorMessage = it.message, code = "700")
        }
    )

    override suspend fun getStoreCommentPresets(storeId: String): Resource<CommentPresetDto> =
        runCatching {
            remoteDataSource.getStoreCommentPresets(
                storeId = storeId
            )
        }.fold(
            onSuccess = {
                if (it.data != null) {
                    Resource.Success(data = it.data!!.toDto(), code = it.code)
                } else {
                    Resource.Error(errorMessage = it.errorMessage, code = it.code)
                }
            },
            onFailure = {
                Resource.Error(errorMessage = it.message, code = "700")
            }
        )

    private suspend fun postNonce(): String = remoteDataSource.postNonce().data!!.nonce
}