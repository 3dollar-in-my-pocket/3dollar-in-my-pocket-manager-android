package app.threedollars.network

import app.threedollars.data.BaseResponse
import app.threedollars.data.request.*
import app.threedollars.data.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {

    // auth-controller
    @POST("v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @POST("v1/auth/logout")
    suspend fun logout(): Response<BaseResponse<String>>

    @POST("v1/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<LoginResponse>>

    @DELETE("v1/auth/signout")
    suspend fun signOut(): Response<BaseResponse<String>>

    // boss-account-controller
    @GET("v1/boss/account/me")
    suspend fun getBossAccount(): Response<BaseResponse<BossAccountInfoResponse>>

    @PUT("v1/boss/account/me")
    suspend fun putBossAccount(@Body bossAccountInfoRequest: BossAccountInfoRequest): Response<BaseResponse<String>>

    // boss-device-controller
    @PUT("v1/device")
    suspend fun putBossDevice(@Body bossDeviceRequest: BossDeviceRequest): Response<BaseResponse<String>>

    @DELETE("v1/device")
    suspend fun deleteBossDevice(): Response<BaseResponse<String>>

    @PUT("v1/device/token")
    suspend fun putBossDeviceToken(@Body bossDeviceRequest: BossDeviceRequest): Response<BaseResponse<String>>

    // boss-store-controller
    @PUT("v1/boss/store/{bossStoreId}")
    suspend fun putBossStore(@Path("bossStoreId") bossStoreId: String, @Body bossStoreRequest: BossStoreRequest): Response<BaseResponse<String>>

    @PATCH("v1/boss/store/{bossStoreId}")
    suspend fun patchBossStore(@Path("bossStoreId") bossStoreId: String, @Body bossStoreRequest: BossStoreRequest): Response<BaseResponse<String>>

    // boss-store-open-controller
    @DELETE("v1/boss/store/{bossStoreId}/close")
    suspend fun deleteBossStoreOpen(@Path("bossStoreId") bossStoreId: String): Response<BaseResponse<String>>

    @POST("v1/boss/store/{bossStoreId}/open")
    suspend fun postBossStoreOpen(
        @Path("bossStoreId") bossStoreId: String,
        @Query("mapLatitude") mapLatitude: Double,
        @Query("mapLongitude") mapLongitude: Double
    ): Response<BaseResponse<String>>

    // boss-store-retrieve-controller
    @GET("v1/boss/store/{bossStoreId}")
    suspend fun getBossStoreRetrieveSpecific(
        @Path("bossStoreId") bossStoreId: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<BaseResponse<BossStoreRetrieveResponse>>

    @GET("v1/boss/store/me")
    suspend fun getBossStoreRetrieveMe(): Response<BaseResponse<BossStoreRetrieveResponse>>

    @GET("v1/boss/stores/around")
    suspend fun getBossStoreRetrieveAround(
        @Query("categoryId") categoryId: String = "",
        @Query("distanceKm") distanceKm: Int = 1,
        @Query("mapLatitude") mapLatitude: Double,
        @Query("mapLongitude") mapLongitude: Double,
        @Query("orderType") orderType: String = "DISTANCE_ASC",
        @Query("size") size: Int = 30
    ): Response<BaseResponse<List<BossStoreRetrieveAroundResponse>>>

    // enum-mapper-controller
    @GET("v1/enums")
    suspend fun getBossEnums(): Response<BaseResponse<BossEnumsResponse>>

    // faq-controller
    @GET("v1/faq/categories")
    suspend fun getFaqCategories(): Response<BaseResponse<List<FaqCategoriesResponse>>>

    @GET("v1/faqs")
    suspend fun getFaqs(@Query("category") category: String = ""): Response<BaseResponse<List<FaqResponse>>>

    // feedback-controller
    @GET("v1/feedback/{targetType}/target/{targetId}/full")
    suspend fun getFeedbackFull(
        @Path("targetType") targetType: String,
        @Path("targetId") targetId: String
    ): Response<BaseResponse<List<FeedbackFullResponse>>>

    @GET("v1/feedback/{targetType}/target/{targetId}/specific")
    suspend fun getFeedbackSpecific(
        @Path("targetType") targetType: String,
        @Path("targetId") targetId: String,
        @Query("startDate") startDAte: String,
        @Query("endDate") endDate: String
    ): Response<BaseResponse<FeedbackSpecificResponse>>

    @GET("v1/feedback/{targetType}/types")
    suspend fun getFeedbackTypes(@Path("targetType") targetType: String): Response<BaseResponse<List<FeedbackTypesResponse>>>

    // image-upload-controller
    @Multipart
    @POST("v1/upload/{fileType}")
    suspend fun postImageUpload(@Path("fileType") fileType: String, @Part file: MultipartBody.Part): Response<BaseResponse<ImageUploadResponse>>

    @Multipart
    @POST("v1/upload/{fileType}/bulk")
    suspend fun postImageUploadBulk(
        @Path("fileType") fileType: String,
        @Part files: List<MultipartBody.Part>
    ): Response<BaseResponse<List<ImageUploadResponse>>>

    // platform-store-category-controller
    @GET("v1/store/{storeType}/categories")
    suspend fun getStoreCategories(@Path("storeType") storeType: String): Response<BaseResponse<List<StoreCategoriesResponse>>>

}