package app.threedollars.data.response


import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.BossAccountInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BossAccountInfoResponse(
    @SerialName("bossId")
    val bossId: String? = "",
    @SerialName("businessNumber")
    val businessNumber: String? = "",
    @SerialName("createdAt")
    val createdAt: String? = "",
    @SerialName("name")
    val name: String? = "",
    @SerialName("socialType")
    val socialType: String? = "",
    @SerialName("settings")
    val settings: Settings? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = "",
) : BaseResponse<BossAccountInfoResponse>() {
    @Serializable
    data class Settings(
        @SerialName("enableActivitiesPush")
        val enableActivitiesPush: Boolean? = false,
        @SerialName("enableSalesAIRecommendation")
        val enableSalesAIRecommendation: Boolean? = false
    )

    fun toDto() = BossAccountInfoDto(
        bossId = bossId.toStringDefault(),
        businessNumber = businessNumber.toStringDefault(),
        createdAt = createdAt.toStringDefault(),
        name = name.toStringDefault(),
        socialType = socialType.toStringDefault(),
        settings = BossAccountInfoDto.Settings(
            enableActivitiesPush = settings?.enableActivitiesPush ?: false,
            enableSalesAIRecommendation = settings?.enableSalesAIRecommendation ?: false
        ),
        updatedAt = updatedAt.toStringDefault()
    )
}