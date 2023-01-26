package app.threedollars.data.user.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.user.model.ContentsModel
import app.threedollars.data.user.model.CursorModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackSpecificResponse(
    @Json(name = "contents")
    val contents: List<ContentsModel>? = null,
    @Json(name = "cursor")
    val cursor: CursorModel? = null,
) : BaseResponse<FeedbackSpecificResponse>()
