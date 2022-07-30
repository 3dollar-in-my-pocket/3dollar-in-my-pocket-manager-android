package app.threedollars.data.user.response


import app.threedollars.data.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignResponse(
    @Json(name = "bossId")
    val bossId: String? = null,
    @Json(name = "token")
    val token: String? = null
) : BaseResponse<SignResponse>()