package app.threedollars.data.user.response

import com.squareup.moshi.Json

data class KakaoTokenResponse(
    @Json(name = "token_type")
    val tokenType: String = "",
    @Json(name = "access_token")
    val accessToken: String = "",
    @Json(name = "expires_in")
    val expiresIn: Int = 0,
    @Json(name = "refresh_token")
    val refreshToken: String = "",
    @Json(name = "refresh_token_expires_in")
    val refreshTokenExpiresIn: Int = 0,
    @Json(name = "scope")
    val scope: String = ""
)