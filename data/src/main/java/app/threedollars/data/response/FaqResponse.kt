package app.threedollars.data.response


import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.model.CategoryInfoModel
import app.threedollars.domain.dto.CategoryInfoDto
import app.threedollars.domain.dto.FaqDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FaqResponse(
    @Json(name = "answer")
    val answer: String? = "",
    @Json(name = "category")
    val category: String? = "",
    @Json(name = "categoryInfo")
    val categoryInfo: CategoryInfoModel? = CategoryInfoModel(),
    @Json(name = "createdAt")
    val createdAt: String? = "",
    @Json(name = "faqId")
    val faqId: Int? = 0,
    @Json(name = "question")
    val question: String? = "",
    @Json(name = "updatedAt")
    val updatedAt: String? = ""
)

fun List<FaqResponse>.toDto() = map {
    FaqDto(
        answer = it.answer.toStringDefault(),
        category = it.category.toStringDefault(),
        categoryInfo = it.categoryInfo?.toDto() ?: CategoryInfoDto(),
        createdAt = it.createdAt.toStringDefault(),
        faqId = it.faqId ?: 0,
        question = it.question.toStringDefault(),
        updatedAt = it.updatedAt.toStringDefault()
    )
}
