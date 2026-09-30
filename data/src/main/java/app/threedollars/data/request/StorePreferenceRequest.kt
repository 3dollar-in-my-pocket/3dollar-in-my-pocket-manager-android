package app.threedollars.data.request

import app.threedollars.domain.dto.StorePreferenceDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StorePreferenceRequest(
    @SerialName("retainLocationOnClose")
    val retainLocationOnClose: Boolean,
    @SerialName("autoOpenCloseControl")
    val autoOpenCloseControl: Boolean,
) {
    companion object {
        fun from(dto: StorePreferenceDto) = StorePreferenceRequest(
            retainLocationOnClose = dto.retainLocationOnClose,
            autoOpenCloseControl = dto.autoOpenCloseControl,
        )
    }
}
