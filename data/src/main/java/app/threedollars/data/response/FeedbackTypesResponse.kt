package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.FeedbackTypesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackTypesResponse(
    @Json(name = "description")
    val description: String = "",
    @Json(name = "emoji")
    val emoji: String = "",
    @Json(name = "feedbackType")
    val feedbackType: String = ""
) : BaseResponse<FeedbackTypesResponse>()

fun List<FeedbackTypesResponse>.toDto() = map {
    FeedbackTypesDto(it.description, it.emoji, it.feedbackType)
}

