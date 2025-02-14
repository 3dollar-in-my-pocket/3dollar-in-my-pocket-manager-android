package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    @SerialName("reason")
    val reason: String = "REVIEW_ETC",
    @SerialName("reasonDetail")
    val reasonDetail: String
)