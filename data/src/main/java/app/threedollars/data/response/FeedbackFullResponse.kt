package app.threedollars.data.response


import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.FeedbackFullDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackFullResponse(
    @Json(name = "count")
    val count: Int? = 0,
    @Json(name = "feedbackType")
    val feedbackType: String? = "",
    @Json(name = "ratio")
    val ratio: String? = ""
) : BaseResponse<FeedbackFullResponse>()

fun List<FeedbackFullResponse>.toDto() = map {
    FeedbackFullDto(
        count = it.count ?: 0,
        feedbackType = it.feedbackType.toStringDefault(),
        ratio = it.ratio.toStringDefault()
    )
}

