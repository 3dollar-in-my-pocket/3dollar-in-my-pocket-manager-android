package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackFullResponse(
    @Json(name = "count")
    val count: Int? = null,
    @Json(name = "feedbackType")
    val feedbackType: String? = null,
    @Json(name = "ratio")
    val ratio: String? = null
) : BaseResponse<FeedbackFullResponse>()
