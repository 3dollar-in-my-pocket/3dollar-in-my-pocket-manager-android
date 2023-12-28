package app.threedollars.data.response


import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.BossAccountInfoDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossAccountInfoResponse(
    @Json(name = "bossId")
    val bossId: String? = "",
    @Json(name = "businessNumber")
    val businessNumber: String? = "",
    @Json(name = "createdAt")
    val createdAt: String? = "",
    @Json(name = "isSetupNotification")
    val isSetupNotification: Boolean? = false,
    @Json(name = "name")
    val name: String? = "",
    @Json(name = "socialType")
    val socialType: String? = "",
    @Json(name = "updatedAt")
    val updatedAt: String? = ""
) : BaseResponse<BossAccountInfoResponse>() {
    fun toDto() = BossAccountInfoDto(
        bossId = bossId.toStringDefault(),
        businessNumber = businessNumber.toStringDefault(),
        createdAt = createdAt.toStringDefault(),
        isSetupNotification = isSetupNotification ?: false,
        name = name.toStringDefault(),
        socialType = socialType.toStringDefault(),
        updatedAt = updatedAt.toStringDefault()
    )
}