package app.threedollars.data.user

import app.threedollars.data.BaseResponse
import app.threedollars.data.user.request.EditMyInfoRequest
import app.threedollars.data.user.request.LoginRequest
import app.threedollars.data.user.request.SignUpRequest
import app.threedollars.data.user.response.BossAccountInfoResponse
import app.threedollars.data.user.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    // auth-controller
    @POST("/v1/auth/login")
    fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @POST("/v1/auth/logout")
    fun logout(): Response<BaseResponse<String>>

    @POST("/v1/auth/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<String>>

    @DELETE("/v1/auth/sign-out")
    fun signOut(): Response<BaseResponse<String>>

    // boss-account-controller
    @GET("v1/boss-account/my")
    fun getMyAccount(): Response<BossAccountInfoResponse>

    @PUT("v1/boss-account/my")
    fun editMyInfoResponse(@Body editMyInfoRequest: EditMyInfoRequest): Response<String>
}