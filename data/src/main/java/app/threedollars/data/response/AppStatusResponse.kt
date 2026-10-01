package app.threedollars.data.response

import app.threedollars.domain.dto.AppForceUpdateDto
import app.threedollars.domain.dto.AppStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AppStatusResponse(
    @SerialName("forceUpdate")
    val forceUpdate: AppForceUpdateResponse = AppForceUpdateResponse(),
) {
    fun toDto() = AppStatusDto(
        forceUpdate = forceUpdate.toDto(),
    )
}

@Serializable
internal data class AppForceUpdateResponse(
    @SerialName("enabled")
    val enabled: Boolean = false,
    @SerialName("title")
    val title: String? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("linkUrl")
    val linkUrl: String? = null,
) {
    fun toDto() = AppForceUpdateDto(
        enabled = enabled,
        title = title,
        message = message,
        linkUrl = linkUrl,
    )
}
