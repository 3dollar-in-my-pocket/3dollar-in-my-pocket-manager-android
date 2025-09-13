package app.threedollars.source

import app.threedollars.common.Resource
import app.threedollars.data.request.BossAccountInfoRequest
import app.threedollars.data.request.BossDeviceRequest
import app.threedollars.data.request.BossStoreRequest
import app.threedollars.data.request.LoginRequest
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
import app.threedollars.data.response.StoreCategoriesResponse
import app.threedollars.data.response.StoreReviewResponse
import app.threedollars.data.response.StoreReviewResponse.StoreReview
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

internal interface RemoteDataSource {
    fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>

    fun logout(): Flow<Resource<String>>

    fun signUp(signUpRequest: SignUpRequest): Flow<Resource<LoginResponse>>

    fun signOut(): Flow<Resource<String>>

    // boss-account-controller
    fun getBossAccount(): Flow<Resource<BossAccountInfoResponse>>

    fun putBossAccount(bossAccountInfoRequest: BossAccountInfoRequest): Flow<Resource<String>>

    // boss-device-controller
    fun putBossDevice(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>>

    fun deleteBossDevice(): Flow<Resource<String>>

    fun putBossDeviceToken(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>>

    // boss-store-controller
    fun putBossStore(
        bossStoreId: String,
        bossStoreRequest: BossStoreRequest,
    ): Flow<Resource<String>>

    fun patchBossStore(
        bossStoreId: String,
        bossStoreRequest: BossStoreRequest,
    ): Flow<Resource<String>>

    // boss-store-open-controller
    fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>>

    fun postBossStoreOpen(
        bossStoreId: String,
        mapLatitude: Double,
        mapLongitude: Double,
    ): Flow<Resource<String>>

    // boss-store-retrieve-controller
    fun getBossStoreRetrieveSpecific(
        bossStoreId: String,
        latitude: Double,
        longitude: Double,
    ): Flow<Resource<BossStoreRetrieveResponse>>

    fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveResponse>>

    fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int,
    ): Flow<Resource<List<BossStoreRetrieveAroundResponse>>>

    // enum-mapper-controller
    fun getBossEnums(): Flow<Resource<BossEnumsResponse>>

    // faq-controller
    fun getFaqCategories(): Flow<Resource<List<FaqCategoriesResponse>>>

    fun getFaqs(category: String = ""): Flow<Resource<List<FaqResponse>>>

    // feedback-controller
    fun getFeedbackFull(
        targetType: String,
        targetId: String,
    ): Flow<Resource<List<FeedbackFullResponse>>>

    fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesResponse>>>

    // image-upload-controller
    fun postImageUpload(
        fileType: String,
        requestBody: RequestBody,
    ): Flow<Resource<ImageUploadResponse>>

    fun postImageUploadBulk(
        fileType: String,
        requestBodyList: List<RequestBody>,
    ): Flow<Resource<List<ImageUploadResponse>>>

    // platform-store-category-controller
    fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesResponse>>>

    fun getStoreReviews(
        storeId: String,
        sort: String,
    ): Flow<Resource<StoreReviewResponse>>

    fun getStoreReviewDetail(
        storeId: String,
        reviewId: String,
    ): Flow<Resource<StoreReview>>

    fun postStoreReviewReport(
        storeId: String,
        reviewId: String,
        reasonDetail: String,
    ): Flow<Resource<String>>

    suspend fun postStoreReviewComment(
        storeId: String,
        reviewId: String,
        nonce: String,
        reviewComment: String,
    ): Resource<CommentCreateResponse>

    suspend fun putStickersReplace(
        storeId: String,
        reviewId: String,
        stickers: String,
    ): Resource<String>

    suspend fun deleteStoreReviewComment(
        storeId: String,
        reviewId: String,
        commentId: String,
    ): Resource<String>

    suspend fun postNonce(): Resource<NonceResponse>

    suspend fun postStoreCommentPreset(
        storeId: String,
        nonce: String,
        body: String,
    ): Resource<CommentPresetResponse.CommentPreset>

    suspend fun deleteStoreCommentPreset(
        storeId: String,
        presetId: String,
    ): Resource<String>

    suspend fun patchStoreCommentPreset(
        storeId: String,
        presetId: String,
        body: String,
    ): Resource<String>

    suspend fun getStoreCommentPresets(
        storeId: String,
    ): Resource<CommentPresetResponse>
}