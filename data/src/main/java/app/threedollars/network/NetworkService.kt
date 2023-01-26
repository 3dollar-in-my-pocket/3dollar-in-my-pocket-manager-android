package app.threedollars.network

import app.threedollars.data.BaseResponse
import app.threedollars.data.request.*
import app.threedollars.data.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {

    // auth-controller
    @POST("/v1/auth/login")
    fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @POST("/v1/auth/logout")
    fun logout(): Response<BaseResponse<String>>

    @POST("/v1/auth/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<String>>

    @DELETE("/v1/auth/signout")
    fun signOut(): Response<BaseResponse<String>>

    // boss-account-controller
    @GET("/v1/boss/account/me")
    fun getBossAccount(): Response<BaseResponse<BossAccountInfoResponse>>

    @PUT("/v1/boss/account/me")
    fun putBossAccount(@Body bossAccountInfoRequest: BossAccountInfoRequest): Response<BaseResponse<String>>

    // boss-device-controller
    @PUT("/v1/device")
    fun putBossDevice(@Body bossDeviceRequest: BossDeviceRequest): Response<BaseResponse<String>>

    @DELETE("/v1/device")
    fun deleteBossDevice(): Response<BaseResponse<String>>

    @PUT("/v1/device/token")
    fun putBossDeviceToken(@Body bossDeviceRequest: BossDeviceRequest): Response<BaseResponse<String>>

    // boss-store-controller
    @PUT("/v1/boss/store/{bossStoreId}")
    fun putBossStore(@Path("bossStoreId") bossStoreId: String, @Body bossStoreRequest: BossStoreRequest): Response<BaseResponse<String>>

    @PATCH("/v1/boss/store/{bossStoreId}")
    fun patchBossStore(@Path("bossStoreId") bossStoreId: String, @Body bossStoreRequest: BossStoreRequest): Response<BaseResponse<String>>

    // boss-store-open-controller
    @DELETE("/v1/boss/store/{bossStoreId}/close")
    fun deleteBossStoreOpen(@Path("bossStoreId") bossStoreId: String): Response<BaseResponse<String>>

    @POST("/v1/boss/store/{bossStoreId}/open")
    fun postBossStoreOpen(
        @Path("bossStoreId") bossStoreId: String,
        @Query("mapLatitude") mapLatitude: Double,
        @Query("mapLongitude") mapLongitude: Double
    ): Response<BaseResponse<String>>

    // boss-store-retrieve-controller
    @GET("/v1/boss/store/{bossStoreId}")
    fun getBossStoreRetrieveSpecific(
        @Path("bossStoreId") bossStoreId: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<BaseResponse<BossStoreRetrieveResponse>>

    @GET("/v1/boss/store/me")
    fun getBossStoreRetrieveMe(): Response<BaseResponse<BossStoreRetrieveResponse>>

    @GET("/v1/boss/store/around")
    fun getBossStoreRetrieveAround(
        @Query("categoryId") categoryId: String = "",
        @Query("distanceKm") distanceKm: Int = 1,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("mapLatitude") mapLatitude: Double,
        @Query("mapLongitude") mapLongitude: Double,
        @Query("orderType") orderType: String = "DISTANCE_ASC",
        @Query("size") size: Int = 30
    ): Response<BaseResponse<List<BossStoreRetrieveAroundResponse>>>

    // enum-mapper-controller
    @GET("v1/enums")
    fun getBossEnums(): Response<BaseResponse<BossEnumsResponse>>

    // faq-controller
    @GET("v1/faq/categories")
    fun getFaqCategories(): Response<BaseResponse<List<FaqCategoriesResponse>>>

    @GET("v1/faqs")
    fun getFaqs(@Query("category") category: String = ""): Response<BaseResponse<List<FaqResponse>>>

    // feedback-controller
    @GET("v1/feedback/{targetType}/target/{targetId}/full")
    fun getFeedbackFull(
        @Path("targetType") targetType: String,
        @Path("targetId") targetId: String
    ): Response<BaseResponse<List<FeedbackFullResponse>>>

    @GET("v1/feedback/{targetType}/target/{targetId}/specific")
    fun getFeedbackSpecific(
        @Path("targetType") targetType: String,
        @Path("targetId") targetId: String,
        @Query("startDate") startDAte: String,
        @Query("endDate") endDate: String
    ): Response<BaseResponse<FeedbackSpecificResponse>>

    @GET("v1/feedback/{targetType}/types")
    fun getFeedbackTypes(@Path("targetType") targetType: String): Response<BaseResponse<List<FeedbackTypesResponse>>>

    // image-upload-controller
    @Multipart
    @POST("v1/upload/{fileType}")
    fun postImageUpload(@Path("fileType") fileType: String, file: MultipartBody.Part): Response<BaseResponse<ImageUploadResponse>>

    @Multipart
    @POST("v1/upload/{fileType}/bulk")
    fun postImageUploadBulk(@Path("fileType") fileType: String, fileList: List<MultipartBody.Part>): Response<BaseResponse<List<ImageUploadResponse>>>

    // platform-store-category-controller
    @GET("v1/store/{storeType}/categories")
    fun getStoreCategories(@Path("storeType") storeType: String) : Response<BaseResponse<List<StoreCategoriesResponse>>>

}