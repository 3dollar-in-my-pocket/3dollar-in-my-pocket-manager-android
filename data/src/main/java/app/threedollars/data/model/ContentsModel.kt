package app.threedollars.data.model


import app.threedollars.domain.dto.ContentsDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentsModel(
    @Json(name = "date")
    val date: String = "",
    @Json(name = "feedbacks")
    val feedbacks: List<FeedbacksModel> = listOf()
)
fun ContentsModel.toDto() = ContentsDto(date, feedbacks.toDto())
