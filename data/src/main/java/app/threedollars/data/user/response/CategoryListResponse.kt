package app.threedollars.data.user.response

import app.threedollars.data.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CategoryListResponse : BaseResponse<Category>()

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "categoryId")
    val bossId: String? = null,
    @Json(name = "name")
    val token: String? = null
) : BaseResponse<List<Category>>()