package app.threedollars.source

import app.threedollars.common.Resource
import app.threedollars.data.BaseResponse
import app.threedollars.data.request.BossAccountInfoRequest
import app.threedollars.data.request.BossDeviceRequest
import app.threedollars.data.request.BossStoreRequest
import app.threedollars.data.request.CommentPresetRequest
import app.threedollars.data.request.LoginRequest
import app.threedollars.data.request.ReportRequest
import app.threedollars.data.request.ReviewCommentRequest
import app.threedollars.data.request.SignUpRequest
import app.threedollars.data.response.BossAccountInfoResponse
import app.threedollars.data.response.BossEnumsResponse
import app.threedollars.data.response.BossStoreRetrieveAroundResponse
import app.threedollars.data.response.BossStoreRetrieveResponse
import app.threedollars.data.response.CommentCreateResponse
import app.threedollars.data.response.CommentPresetResponse
import app.threedollars.data.response.FaqCategoriesResponse
import app.threedollars.data.response.FaqResponse
import app.threedollars.data.response.FeedbackFullResponse
import app.threedollars.data.response.FeedbackTypesResponse
import app.threedollars.data.response.ImageUploadResponse
import app.threedollars.data.response.LoginResponse
import app.threedollars.data.response.NonceResponse
import app.threedollars.data.response.StickersReplaceRequest
import app.threedollars.data.response.StoreCategoriesResponse
import app.threedollars.data.response.StoreReviewResponse
import app.threedollars.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(private val networkService: NetworkService) :
    RemoteDataSource {
    override fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        emit(safeApiCall(networkService.login(loginRequest)))
    }

    override fun logout(): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.logout()))
    }

    override fun signUp(signUpRequest: SignUpRequest): Flow<Resource<LoginResponse>> = flow {
        emit(safeApiCall(networkService.signUp(signUpRequest)))
    }

    override fun signOut(): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.signOut()))
    }

    override fun getBossAccount(): Flow<Resource<BossAccountInfoResponse>> = flow {
        emit(safeApiCall(networkService.getBossAccount()))
    }

    override fun putBossAccount(bossAccountInfoRequest: BossAccountInfoRequest): Flow<Resource<String>> =
        flow {
            emit(safeApiCall(networkService.putBossAccount(bossAccountInfoRequest)))
        }

    override fun putBossDevice(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>> =
        flow {
            emit(safeApiCall(networkService.putBossDevice(bossDeviceRequest)))
        }

    override fun deleteBossDevice(): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.deleteBossDevice()))
    }

    override fun putBossDeviceToken(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>> =
        flow {
            emit(safeApiCall(networkService.putBossDeviceToken(bossDeviceRequest)))
        }

    override fun putBossStore(
        bossStoreId: String,
        bossStoreRequest: BossStoreRequest,
    ): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.putBossStore(bossStoreId, bossStoreRequest)))
    }

    override fun patchBossStore(
        bossStoreId: String,
        bossStoreRequest: BossStoreRequest,
    ): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.patchBossStore(bossStoreId, bossStoreRequest)))
    }

    override fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.deleteBossStoreOpen(bossStoreId)))
    }

    override fun postBossStoreOpen(
        bossStoreId: String,
        mapLatitude: Double,
        mapLongitude: Double,
    ): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.postBossStoreOpen(bossStoreId, mapLatitude, mapLongitude)))
    }

    override fun getBossStoreRetrieveSpecific(
        bossStoreId: String,
        latitude: Double,
        longitude: Double,
    ): Flow<Resource<BossStoreRetrieveResponse>> = flow {
        emit(
            safeApiCall(
                networkService.getBossStoreRetrieveSpecific(
                    bossStoreId,
                    latitude,
                    longitude
                )
            )
        )
    }

    override fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveResponse>> = flow {
        emit(safeApiCall(networkService.getBossStoreRetrieveMe()))
    }

    override fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int,
    ): Flow<Resource<List<BossStoreRetrieveAroundResponse>>> = flow {
        emit(
            safeApiCall(
                networkService.getBossStoreRetrieveAround(
                    categoryId,
                    distanceKm,
                    mapLatitude,
                    mapLongitude,
                    orderType,
                    size
                )
            )
        )
    }

    override fun getBossEnums(): Flow<Resource<BossEnumsResponse>> = flow {
        emit(safeApiCall(networkService.getBossEnums()))
    }

    override fun getFaqCategories(): Flow<Resource<List<FaqCategoriesResponse>>> = flow {
        emit(safeApiCall(networkService.getFaqCategories()))
    }

    override fun getFaqs(category: String): Flow<Resource<List<FaqResponse>>> = flow {
        emit(safeApiCall(networkService.getFaqs(category)))
    }

    override fun getFeedbackFull(
        targetType: String,
        targetId: String,
    ): Flow<Resource<List<FeedbackFullResponse>>> = flow {
        emit(safeApiCall(networkService.getFeedbackFull(targetType, targetId)))
    }

    override fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesResponse>>> =
        flow {
            emit(safeApiCall(networkService.getFeedbackTypes(targetType)))
        }

    override fun postImageUpload(
        fileType: String,
        requestBody: RequestBody,
    ): Flow<Resource<ImageUploadResponse>> {
        return flow {
            val multipartBody = MultipartBody.Part.createFormData(
                name = "file",
                filename = "file.jpeg",
                body = requestBody
            )
            emit(safeApiCall(networkService.postImageUpload(fileType, multipartBody)))
        }
    }

    override fun postImageUploadBulk(
        fileType: String,
        requestBodyList: List<RequestBody>,
    ): Flow<Resource<List<ImageUploadResponse>>> =
        flow {
            val multipartBodyList = requestBodyList.mapIndexed { index, requestBody ->
                MultipartBody.Part.createFormData(
                    name = "files",
                    filename = "file$index.jpeg",
                    body = requestBody
                )
            }
            emit(safeApiCall(networkService.postImageUploadBulk(fileType, multipartBodyList)))
        }

    override fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesResponse>>> =
        flow {
            emit(safeApiCall(networkService.getStoreCategories(storeType)))
        }

    override fun getStoreReviews(
        storeId: String,
        sort: String,
    ): Flow<Resource<StoreReviewResponse>> = flow {
        emit(
            safeApiCall(
                networkService.getStoreReviews(
                    storeId = storeId,
                    sort = sort,
                )
            )
        )
    }

    override fun getStoreReviewDetail(
        storeId: String,
        reviewId: String,
    ): Flow<Resource<StoreReviewResponse.StoreReview>> = flow {
        emit(
            safeApiCall(
                networkService.getStoreReviewDetail(
                    storeId = storeId,
                    reviewId = reviewId
                )
            )
        )
    }

    override fun postStoreReviewReport(
        storeId: String,
        reviewId: String,
        reasonDetail: String,
    ): Flow<Resource<String>> = flow {
        emit(
            safeApiCall(
                networkService.postStoreReviewReport(
                    storeId = storeId,
                    reviewId = reviewId,
                    reportRequest = ReportRequest(
                        reasonDetail = reasonDetail
                    )
                )
            )
        )
    }

    override suspend fun postStoreReviewComment(
        storeId: String,
        reviewId: String,
        nonce: String,
        reviewComment: String,
    ): Resource<CommentCreateResponse> = safeApiCall(
        networkService.postStoreReviewComment(
            storeId = storeId,
            reviewId = reviewId,
            nonce = nonce,
            reviewCommentRequest = ReviewCommentRequest(
                content = reviewComment
            )
        )
    )

    override suspend fun putStickersReplace(
        storeId: String,
        reviewId: String,
        stickers: String,
    ): Resource<String> = safeApiCall(
        networkService.putStickersReplace(
            storeId = storeId,
            reviewId = reviewId,
            stickersReplaceRequest = StickersReplaceRequest(
                stickers = if (stickers.isEmpty()) listOf() else listOf(StickersReplaceRequest.Sticker(stickerId = stickers))
            )
        )
    )

    override suspend fun deleteStoreReviewComment(
        storeId: String,
        reviewId: String,
        commentId: String,
    ): Resource<String> = safeApiCall(
        networkService.deleteStoreReviewComment(
            storeId = storeId,
            reviewId = reviewId,
            commentId = commentId
        )
    )

    override suspend fun postNonce(): Resource<NonceResponse> = safeApiCall(
        networkService.postNonce()
    )

    override suspend fun postStoreCommentPreset(
        storeId: String,
        nonce: String,
        body: String,
    ): Resource<CommentPresetResponse.CommentPreset> = safeApiCall(
        networkService.postStoreCommentPreset(
            storeId = storeId,
            nonce = nonce,
            commentPresetRequest = CommentPresetRequest(body = body)
        )
    )

    override suspend fun deleteStoreCommentPreset(
        storeId: String,
        presetId: String,
    ): Resource<String> = safeApiCall(
        networkService.deleteStoreCommentPreset(
            storeId = storeId,
            presetId = presetId
        )
    )

    override suspend fun patchStoreCommentPreset(
        storeId: String,
        presetId: String,
        body: String,
    ): Resource<String> = safeApiCall(
        networkService.patchStoreCommentPreset(
            storeId = storeId,
            presetId = presetId,
            commentPresetRequest = CommentPresetRequest(body = body)
        )
    )

    override suspend fun getStoreCommentPresets(storeId: String): Resource<CommentPresetResponse> =
        safeApiCall(
            networkService.getStoreCommentPresets(
                storeId = storeId
            )
        )
}

fun <T> safeApiCall(response: Response<BaseResponse<T>>): Resource<T> {
    return try {
        if (response.isSuccessful) {
            Resource.Success(data = response.body()?.data!!, code = response.code().toString())
        } else {
            Resource.Error(
                errorMessage = response.errorBody()?.string(),
                code = response.code().toString()
            )
        }

    } catch (e: HttpException) {
        Resource.Error(errorMessage = e.message ?: "Something went wrong", code = null)
    } catch (e: IOException) {
        Resource.Error(
            errorMessage = e.message ?: "Please check your network connection",
            code = null
        )
    } catch (e: Exception) {
        Resource.Error(errorMessage = e.message ?: "Something went wrong", code = null)
    }
}
