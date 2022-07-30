package app.threedollars.data.user

import app.threedollars.data.BaseResponse
import app.threedollars.data.user.request.EditMyInfoRequest
import app.threedollars.data.user.request.LoginRequest
import app.threedollars.data.user.request.SignUpRequest
import app.threedollars.data.user.response.BossAccountInfoResponse
import app.threedollars.data.user.response.SignResponse
import app.threedollars.data.user.response.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    // auth-controller
    @POST("/boss/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<SignResponse>

    @POST("/boss/v1/auth/logout")
    suspend fun logout(): Response<BaseResponse<String>>

    @POST("/boss/v1/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<SignResponse>>

    @DELETE("/boss/v1/auth/sign-out")
    suspend fun signOut(): Response<BaseResponse<String>>

    // boss-account-controller
    @GET("/boss/v1/boss-account/my")
    suspend fun getMyAccount(): Response<BossAccountInfoResponse>

    @PUT("/boss/v1/boss-account/my")
    suspend fun editMyInfoResponse(@Body editMyInfoRequest: EditMyInfoRequest): Response<String>

    @POST("/boss/v1/upload/{fileType}")
    suspend fun saveStoreImage(
        file: MultipartBody.Part,
        fileType: String = "BOSS_STORE_CERTIFICATION_IMAGE",
    ): Response<UploadImageResponse>
}