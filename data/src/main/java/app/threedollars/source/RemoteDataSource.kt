package app.threedollars.source

import app.threedollars.data.BaseResponse
import app.threedollars.data.request.*
import app.threedollars.data.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response

interface RemoteDataSource {
    fun login(loginRequest: LoginRequest): Flow<Response<BaseResponse<LoginResponse>>>

    fun logout(): Flow<Response<BaseResponse<String>>>

    fun signUp(signUpRequest: SignUpRequest): Flow<Response<BaseResponse<String>>>

    fun signOut(): Flow<Response<BaseResponse<String>>>

    // boss-account-controller
    fun getBossAccount(): Flow<Response<BaseResponse<BossAccountInfoResponse>>>

    fun putBossAccount(bossAccountInfoRequest: BossAccountInfoRequest): Flow<Response<BaseResponse<String>>>

    // boss-device-controller
    fun putBossDevice(bossDeviceRequest: BossDeviceRequest): Flow<Response<BaseResponse<String>>>

    fun deleteBossDevice(): Flow<Response<BaseResponse<String>>>

    fun putBossDeviceToken(bossDeviceRequest: BossDeviceRequest): Flow<Response<BaseResponse<String>>>

    // boss-store-controller
    fun putBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Response<BaseResponse<String>>>

    fun patchBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Response<BaseResponse<String>>>

    // boss-store-open-controller
    fun deleteBossStoreOpen(bossStoreId: String): Flow<Response<BaseResponse<String>>>

    fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Response<BaseResponse<String>>>

    // boss-store-retrieve-controller
    fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Flow<Response<BaseResponse<BossStoreRetrieveResponse>>>

    fun getBossStoreRetrieveMe(): Flow<Response<BaseResponse<BossStoreRetrieveResponse>>>

    fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        latitude: Double,
        longitude: Double,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int
    ): Flow<Response<BaseResponse<List<BossStoreRetrieveAroundResponse>>>>

    // enum-mapper-controller
    fun getBossEnums(): Flow<Response<BaseResponse<BossEnumsResponse>>>

    // faq-controller
    fun getFaqCategories(): Flow<Response<BaseResponse<List<FaqCategoriesResponse>>>>

    fun getFaqs(category: String = ""): Flow<Response<BaseResponse<List<FaqResponse>>>>

    // feedback-controller
    fun getFeedbackFull(targetType: String, targetId: String): Flow<Response<BaseResponse<List<FeedbackFullResponse>>>>

    fun getFeedbackSpecific(
        targetType: String,
        targetId: String,
        startDAte: String,
        endDate: String
    ): Flow<Response<BaseResponse<FeedbackSpecificResponse>>>

    fun getFeedbackTypes(targetType: String): Flow<Response<BaseResponse<List<FeedbackTypesResponse>>>>

    // image-upload-controller
    fun postImageUpload(fileType: String, file: MultipartBody.Part): Flow<Response<BaseResponse<ImageUploadResponse>>>

    fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Flow<Response<BaseResponse<List<ImageUploadResponse>>>>

    // platform-store-category-controller
    fun getStoreCategories(storeType: String): Flow<Response<BaseResponse<List<StoreCategoriesResponse>>>>
}