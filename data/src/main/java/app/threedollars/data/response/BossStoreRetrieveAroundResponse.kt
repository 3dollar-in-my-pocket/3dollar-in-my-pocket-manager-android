package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.*
import app.threedollars.domain.dto.BossAccountInfoDto
import app.threedollars.domain.dto.BossStoreRetrieveAroundDto
import app.threedollars.domain.dto.OpenStatusDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossStoreRetrieveAroundResponse(
    @Json(name = "bossStoreId")
    val bossStoreId: String? = null,
    @Json(name = "categories")
    val categories: List<CategoriesModel>? = listOf(),
    @Json(name = "createdAt")
    val createdAt: String? = null,
    @Json(name = "distance")
    val distance: Int? = null,
    @Json(name = "location")
    val location: LocationModel? = null,
    @Json(name = "menus")
    val menus: List<MenusModel>? = listOf(),
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "openStatus")
    val openStatus: OpenStatusModel?,
    @Json(name = "totalFeedbacksCounts")
    val totalFeedbacksCounts: Int? = null,
    @Json(name = "updatedAt")
    val updatedAt: String? = null
) : BaseResponse<BossStoreRetrieveAroundResponse>()

fun List<BossStoreRetrieveAroundResponse>.toDto() = map {
    BossStoreRetrieveAroundDto(
        bossStoreId = it.bossStoreId,
        categories = it.categories?.toDto() ?: listOf(),
        createdAt = it.createdAt,
        distance = it.distance,
        location = it.location?.toDto(),
        menus = it.menus?.toDto() ?: listOf(),
        name = it.name,
        openStatus = it.openStatus?.toDto() ?: OpenStatusDto(),
        totalFeedbacksCounts = it.totalFeedbacksCounts,
        updatedAt = it.updatedAt
    )
}
