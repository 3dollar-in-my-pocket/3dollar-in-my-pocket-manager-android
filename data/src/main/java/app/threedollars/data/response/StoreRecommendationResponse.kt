package app.threedollars.data.response

import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.StoreRecommendationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreRecommendationResponse(
    @SerialName("text")
    val text: String? = ""
) : BaseResponse<StoreRecommendationResponse>() {
    fun toDto() = StoreRecommendationDto(
        text = text.toStringDefault()
    )
}
