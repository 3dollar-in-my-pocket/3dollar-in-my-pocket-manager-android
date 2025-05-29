package app.threedollars.network

import app.threedollars.data.BaseResponse
import app.threedollars.data.request.BossAccountInfoRequest
import app.threedollars.data.request.BossDeviceRequest
import app.threedollars.data.request.BossStoreRequest
import app.threedollars.data.request.CommentPresetRequest
import app.threedollars.data.request.LoginRequest
import app.threedollars.data.request.NonceRequest
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
import app.threedollars.data.response.FeedbackSpecificResponse
import app.threedollars.data.response.FeedbackTypesResponse
import app.threedollars.data.response.ImageUploadResponse
import app.threedollars.data.response.LoginResponse
import app.threedollars.data.response.NonceResponse
import app.threedollars.data.response.StickersReplaceRequest
import app.threedollars.data.response.StoreCategoriesResponse
import app.threedollars.data.response.StoreReviewResponse
import app.threedollars.data.response.StoreReviewResponse.StoreReview
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

internal interface NetworkService {

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
    suspend fun putBossStore(
        @Path("bossStoreId") bossStoreId: String,
        @Body bossStoreRequest: BossStoreRequest,
    ): Response<BaseResponse<String>>

    @PATCH("v1/boss/store/{bossStoreId}")
    suspend fun patchBossStore(
        @Path("bossStoreId") bossStoreId: String,
        @Body bossStoreRequest: BossStoreRequest,
    ): Response<BaseResponse<String>>

    // boss-store-open-controller
    @DELETE("v1/boss/store/{bossStoreId}/close")
    suspend fun deleteBossStoreOpen(@Path("bossStoreId") bossStoreId: String): Response<BaseResponse<String>>

    @POST("v1/boss/store/{bossStoreId}/open")
    suspend fun postBossStoreOpen(
        @Path("bossStoreId") bossStoreId: String,
        @Query("mapLatitude") mapLatitude: Double,
        @Query("mapLongitude") mapLongitude: Double,
    ): Response<BaseResponse<String>>

    // boss-store-retrieve-controller
    @GET("v1/boss/store/{bossStoreId}")
    suspend fun getBossStoreRetrieveSpecific(
        @Path("bossStoreId") bossStoreId: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
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
        @Query("size") size: Int = 30,
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
        @Path("targetId") targetId: String,
    ): Response<BaseResponse<List<FeedbackFullResponse>>>

    @GET("v1/feedback/{targetType}/target/{targetId}/specific")
    suspend fun getFeedbackSpecific(
        @Path("targetType") targetType: String,
        @Path("targetId") targetId: String,
        @Query("startDate") startDAte: String,
        @Query("endDate") endDate: String,
    ): Response<BaseResponse<FeedbackSpecificResponse>>

    @GET("v1/feedback/{targetType}/types")
    suspend fun getFeedbackTypes(@Path("targetType") targetType: String): Response<BaseResponse<List<FeedbackTypesResponse>>>

    // image-upload-controller
    @Multipart
    @POST("v1/upload/{fileType}")
    suspend fun postImageUpload(
        @Path("fileType") fileType: String,
        @Part file: MultipartBody.Part,
    ): Response<BaseResponse<ImageUploadResponse>>

    @Multipart
    @POST("v1/upload/{fileType}/bulk")
    suspend fun postImageUploadBulk(
        @Path("fileType") fileType: String,
        @Part files: List<MultipartBody.Part>,
    ): Response<BaseResponse<List<ImageUploadResponse>>>

    // platform-store-category-controller
    @GET("v1/store/{storeType}/categories")
    suspend fun getStoreCategories(@Path("storeType") storeType: String): Response<BaseResponse<List<StoreCategoriesResponse>>>

    @GET("v1/store/{storeId}/reviews")
    suspend fun getStoreReviews(
        @Path("storeId") storeId: String,
        @Query("sort") sort: String,
        @Query("size") size: Int? = null,
        @Query("cursor") cursor: String? = null,
    ): Response<BaseResponse<StoreReviewResponse>>

    @GET("v1/store/{storeId}/review/{reviewId}")
    suspend fun getStoreReviewDetail(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
    ): Response<BaseResponse<StoreReview>>

    @POST("v1/store/{storeId}/review/{reviewId}/report")
    suspend fun postStoreReviewReport(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Body reportRequest: ReportRequest,
    ): Response<BaseResponse<String>>

    @POST("v1/nonce")
    suspend fun postNonce(
        @Body nonceRequest: NonceRequest = NonceRequest(),
    ): Response<BaseResponse<NonceResponse>>

    @POST("v1/store/{storeId}/review/{reviewId}/comment")
    suspend fun postStoreReviewComment(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Header("X-Nonce-Token") nonce: String,
        @Body reviewCommentRequest: ReviewCommentRequest,
    ): Response<BaseResponse<CommentCreateResponse>>

    @PUT("v1/store/{storeId}/review/{reviewId}/stickers")
    suspend fun putStickersReplace(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Body stickersReplaceRequest: StickersReplaceRequest,
    ): Response<BaseResponse<String>>

    @DELETE("v1/store/{storeId}/review/{reviewId}/comment/{commentId}")
    suspend fun deleteStoreReviewComment(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Path("commentId") commentId: String,
    ): Response<BaseResponse<String>>

    @POST("v1/store/{storeId}/comment-preset")
    suspend fun postStoreCommentPreset(
        @Path("storeId") storeId: String,
        @Header("X-Nonce-Token") nonce: String,
        @Body commentPresetRequest: CommentPresetRequest,
    ): Response<BaseResponse<CommentPresetResponse.CommentPreset>>

    @DELETE("v1/store/{storeId}/comment-preset/{presetId}")
    suspend fun deleteStoreCommentPreset(
        @Path("storeId") storeId: String,
        @Path("presetId") presetId: String,
    ): Response<BaseResponse<String>>

    @PATCH("v1/store/{storeId}/comment-preset/{presetId}")
    suspend fun patchStoreCommentPreset(
        @Path("storeId") storeId: String,
        @Path("presetId") presetId: String,
        @Body commentPresetRequest: CommentPresetRequest,
    ): Response<BaseResponse<String>>

    @GET("v1/store/{storeId}/comment-presets")
    suspend fun getStoreCommentPresets(
        @Path("storeId") storeId: String,
    ): Response<BaseResponse<CommentPresetResponse>>
}