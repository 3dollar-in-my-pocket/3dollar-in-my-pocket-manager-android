package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.dto.FeedbackTypesDto
import app.threedollars.dto.ImageUploadDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageUploadResponse(
    @Json(name = "imageUrl")
    val imageUrl: String? = null
) : BaseResponse<ImageUploadResponse>(){
    fun toDto() = ImageUploadDto(imageUrl)
}
