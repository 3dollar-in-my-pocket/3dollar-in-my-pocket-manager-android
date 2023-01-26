package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossAccountInfoResponse(
    @Json(name = "bossId")
    val bossId: String? = null,
    @Json(name = "businessNumber")
    val businessNumber: String? = null,
    @Json(name = "createdAt")
    val createdAt: String? = null,
    @Json(name = "isSetupNotification")
    val isSetupNotification: Boolean? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "socialType")
    val socialType: String? = null,
    @Json(name = "updatedAt")
    val updatedAt: String? = null
) : BaseResponse<BossAccountInfoResponse>()
