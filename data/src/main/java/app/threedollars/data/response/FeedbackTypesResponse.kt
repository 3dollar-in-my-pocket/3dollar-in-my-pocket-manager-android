package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.toDto
import app.threedollars.dto.FeedbackSpecificDto
import app.threedollars.dto.FeedbackTypesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackTypesResponse(
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "emoji")
    val emoji: String? = null,
    @Json(name = "feedbackType")
    val feedbackType: String? = null
) : BaseResponse<FeedbackTypesResponse>(){
    fun toDto() = FeedbackTypesDto(description, emoji, feedbackType)

}
