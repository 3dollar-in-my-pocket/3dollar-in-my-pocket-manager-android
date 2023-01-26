package app.threedollars.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentsModel(
    @Json(name = "date")
    val date: String? = null,
    @Json(name = "feedbacks")
    val feedbacks: List<FeedbacksModel> = listOf()
)