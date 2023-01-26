package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.*
import app.threedollars.dto.BossAccountInfoDto
import app.threedollars.dto.BossStoreRetrieveAroundDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossStoreRetrieveAroundResponse(
    @Json(name = "bossStoreId")
    val bossStoreId: String? = null,
    @Json(name = "categories")
    val categories: List<CategoriesModel> = listOf(),
    @Json(name = "createdAt")
    val createdAt: String? = null,
    @Json(name = "distance")
    val distance: Int? = null,
    @Json(name = "location")
    val location: LocationModel? = null,
    @Json(name = "menus")
    val menus: List<MenusModel> = listOf(),
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "openStatus")
    val openStatus: OpenStatusModel,
    @Json(name = "totalFeedbacksCounts")
    val totalFeedbacksCounts: Int? = null,
    @Json(name = "updatedAt")
    val updatedAt: String? = null
) : BaseResponse<BossStoreRetrieveAroundResponse>() {
    fun toDto() = BossStoreRetrieveAroundDto(
        bossStoreId,
        categories.toDto(),
        createdAt,
        distance,
        location?.toDto(),
        menus.toDto(),
        name,
        openStatus.toDto(),
        totalFeedbacksCounts,
        updatedAt
    )
}
