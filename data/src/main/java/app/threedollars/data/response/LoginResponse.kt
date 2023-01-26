package app.threedollars.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "bossId")
    val bossId: String? = null,
    @Json(name = "token")
    val token: String? = null
)