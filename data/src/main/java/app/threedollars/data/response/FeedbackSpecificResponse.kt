package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.ContentsModel
import app.threedollars.data.model.CursorModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackSpecificResponse(
    @Json(name = "contents")
    val contents: List<ContentsModel>? = null,
    @Json(name = "cursor")
    val cursor: CursorModel? = null,
) : BaseResponse<FeedbackSpecificResponse>()
