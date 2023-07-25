package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.ContentsModel
import app.threedollars.data.model.CursorModel
import app.threedollars.data.model.toDto
import app.threedollars.domain.dto.FeedbackSpecificDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackSpecificResponse(
    @Json(name = "contents")
    val contents: List<ContentsModel> = listOf(),
    @Json(name = "cursor")
    val cursor: CursorModel = CursorModel(),
) : BaseResponse<FeedbackSpecificResponse>() {

}
