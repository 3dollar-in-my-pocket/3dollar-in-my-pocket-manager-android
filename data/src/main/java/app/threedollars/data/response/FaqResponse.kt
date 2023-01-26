package app.threedollars.data.response


import app.threedollars.data.model.CategoryInfoModel
import app.threedollars.dto.BossAccountInfoDto
import app.threedollars.dto.FaqDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FaqResponse(
    @Json(name = "answer")
    val answer: String? = null,
    @Json(name = "category")
    val category: String? = null,
    @Json(name = "categoryInfo")
    val categoryInfo: CategoryInfoModel? = null,
    @Json(name = "createdAt")
    val createdAt: String? = null,
    @Json(name = "faqId")
    val faqId: Int? = null,
    @Json(name = "question")
    val question: String? = null,
    @Json(name = "updatedAt")
    val updatedAt: String? = null
){
    fun toDto() = FaqDto(answer, category, categoryInfo?.toDto(), createdAt, faqId, question, updatedAt)

}