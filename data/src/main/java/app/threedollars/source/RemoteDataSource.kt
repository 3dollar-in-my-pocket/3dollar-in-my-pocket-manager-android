package app.threedollars.source

import app.threedollars.common.Resource
import app.threedollars.data.request.*
import app.threedollars.data.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface RemoteDataSource {
    fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>

    fun logout(): Flow<Resource<String>>

    fun signUp(signUpRequest: SignUpRequest): Flow<Resource<String>>

    fun signOut(): Flow<Resource<String>>
    // boss-account-controller
    fun getBossAccount(): Flow<Resource<BossAccountInfoResponse>>

    fun putBossAccount(bossAccountInfoRequest: BossAccountInfoRequest): Flow<Resource<String>>

    // boss-device-controller
    fun putBossDevice(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>>

    fun deleteBossDevice(): Flow<Resource<String>>

    fun putBossDeviceToken(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>>

    // boss-store-controller
    fun putBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Resource<String>>

    fun patchBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Resource<String>>

    // boss-store-open-controller
    fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>>

    fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Resource<String>>

    // boss-store-retrieve-controller
    fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Flow<Resource<BossStoreRetrieveResponse>>

    fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveResponse>>

    fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        latitude: Double,
        longitude: Double,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int
    ): Flow<Resource<List<BossStoreRetrieveAroundResponse>>>

    // enum-mapper-controller
    fun getBossEnums(): Flow<Resource<BossEnumsResponse>>

    // faq-controller
    fun getFaqCategories(): Flow<Resource<List<FaqCategoriesResponse>>>

    fun getFaqs(category: String = ""): Flow<Resource<List<FaqResponse>>>

    // feedback-controller
    fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullResponse>>>

    fun getFeedbackSpecific(
        targetType: String,
        targetId: String,
        startDAte: String,
        endDate: String
    ): Flow<Resource<FeedbackSpecificResponse>>

    fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesResponse>>>

    // image-upload-controller
    fun postImageUpload(fileType: String, file: MultipartBody.Part): Flow<Resource<ImageUploadResponse>>

    fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Flow<Resource<List<ImageUploadResponse>>>

    // platform-store-category-controller
    fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesResponse>>>
}