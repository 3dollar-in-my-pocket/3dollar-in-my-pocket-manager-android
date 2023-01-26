package app.threedollars.data.model


import app.threedollars.dto.FeedbacksDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbacksModel(
    @Json(name = "count")
    val count: String? = null,
    @Json(name = "feedbackType")
    val feedbackType: String? = ""
)
fun List<FeedbacksModel>.toDto() = map {
    FeedbacksDto(it.count, it.feedbackType)
}
