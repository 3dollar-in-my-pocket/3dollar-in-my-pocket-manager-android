package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.FeedbackFullDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackFullResponse(
    @Json(name = "count")
    val count: Int = 0,
    @Json(name = "feedbackType")
    val feedbackType: String = "",
    @Json(name = "ratio")
    val ratio: String = ""
) : BaseResponse<FeedbackFullResponse>()

fun List<FeedbackFullResponse>.toDto() = map {
    FeedbackFullDto(it.count, it.feedbackType, it.ratio)
}

