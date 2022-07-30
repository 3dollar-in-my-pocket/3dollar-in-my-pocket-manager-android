package app.threedollars.data.user.response


import app.threedollars.data.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadImageResponse(
    @Json(name = "imageUrl")
    val imageUrl: String? = null
) : BaseResponse<UploadImageResponse>()