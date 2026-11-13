package app.threedollars.data.request

import app.threedollars.domain.dto.BossAccountInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BossAccountSettingsRequest(
    @SerialName("enableActivitiesPush")
    val enableActivitiesPush: Boolean,
    @SerialName("pushQuietHours")
    val pushQuietHours: PushQuietHoursRequest,
) {
    @Serializable
    data class PushQuietHoursRequest(
        @SerialName("enabled")
        val enabled: Boolean,
        @SerialName("startTime")
        val startTime: String? = null,
        @SerialName("endTime")
        val endTime: String? = null,
    )

    companion object {
        fun of(enableActivitiesPush: Boolean, pushQuietHours: BossAccountInfoDto.PushQuietHours?) = BossAccountSettingsRequest(
            enableActivitiesPush = enableActivitiesPush,
            pushQuietHours = PushQuietHoursRequest(
                enabled = pushQuietHours != null,
                startTime = pushQuietHours?.startTime,
                endTime = pushQuietHours?.endTime,
            ),
        )
    }
}
