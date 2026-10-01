package app.threedollars.data.response

import app.threedollars.domain.dto.StorePreferenceDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StorePreferenceResponse(
    @SerialName("retainLocationOnClose")
    val retainLocationOnClose: Boolean? = null,
    @SerialName("retainLocationOnCloseSettingTime")
    val retainLocationOnCloseSettingTime: String? = null,
    @SerialName("autoOpenCloseControl")
    val autoOpenCloseControl: Boolean? = null,
    @SerialName("autoOpenCloseControlSettingTime")
    val autoOpenCloseControlSettingTime: String? = null,
) {
    fun toDto() = StorePreferenceDto(
        retainLocationOnClose = retainLocationOnClose ?: false,
        autoOpenCloseControl = autoOpenCloseControl ?: false,
    )
}
