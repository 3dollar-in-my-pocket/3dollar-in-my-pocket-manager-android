package app.threedollars.data.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbacksModel(
    @Json(name = "count")
    val count: String? = null,
    @Json(name = "feedbackType")
    val feedbackType: String? = ""
)