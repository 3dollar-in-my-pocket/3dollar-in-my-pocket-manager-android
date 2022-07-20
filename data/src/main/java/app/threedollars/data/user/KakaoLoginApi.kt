package app.threedollars.data.user

import app.threedollars.data.BuildConfig
import app.threedollars.data.user.response.KakaoTokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KakaoLoginApi {

    @FormUrlEncoded
    @POST("/oauth/token")
    suspend fun refreshToken(
        @Field("refresh_token")
        refreshToken: String,
        @Field("client_id")
        clientId: String = BuildConfig.KAKAO_AUTH_KEY,
        @Field("grant_type")
        grantType: String,
    ): Response<KakaoTokenResponse>
}