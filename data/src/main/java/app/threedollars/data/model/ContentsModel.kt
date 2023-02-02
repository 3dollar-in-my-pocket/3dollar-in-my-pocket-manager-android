package app.threedollars.data.model


import app.threedollars.domain.dto.ContentsDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentsModel(
    @Json(name = "date")
    val date: String? = null,
    @Json(name = "feedbacks")
    val feedbacks: List<FeedbacksModel> = listOf()
)
fun List<ContentsModel>.toDto() = map {
    ContentsDto(it.date, it.feedbacks.toDto())
}
