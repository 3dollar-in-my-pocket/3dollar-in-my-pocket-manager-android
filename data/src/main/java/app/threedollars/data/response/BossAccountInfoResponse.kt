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
        val enableSalesAIRecommendation: Boolean? = false,
        @SerialName("pushQuietHours")
        val pushQuietHours: PushQuietHours? = null,
    )

    @Serializable
    data class PushQuietHours(
        @SerialName("startTime")
        val startTime: String? = null,
        @SerialName("endTime")
        val endTime: String? = null,
    ) {
        fun toDtoOrNull(): BossAccountInfoDto.PushQuietHours? {
            if (startTime.isNullOrEmpty() || endTime.isNullOrEmpty()) return null
            return BossAccountInfoDto.PushQuietHours(startTime = startTime, endTime = endTime)
        }
    }

    fun toDto() = BossAccountInfoDto(
        bossId = bossId.toStringDefault(),
        businessNumber = businessNumber.toStringDefault(),
        createdAt = createdAt.toStringDefault(),
        name = name.toStringDefault(),
        socialType = socialType.toStringDefault(),
        settings = BossAccountInfoDto.Settings(
            enableActivitiesPush = settings?.enableActivitiesPush ?: false,
            enableSalesAIRecommendation = settings?.enableSalesAIRecommendation ?: false,
            pushQuietHours = settings?.pushQuietHours?.toDtoOrNull(),
        ),
        updatedAt = updatedAt.toStringDefault()
    )
}