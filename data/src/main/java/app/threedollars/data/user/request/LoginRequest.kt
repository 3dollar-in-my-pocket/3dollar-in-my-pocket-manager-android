package app.threedollars.data.user.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "socialType")
    val socialType: String? = null,
    @Json(name = "token")
    val token: String? = null
)