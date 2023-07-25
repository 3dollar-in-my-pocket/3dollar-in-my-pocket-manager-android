package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.model.*
import app.threedollars.domain.dto.BossStoreRetrieveDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossStoreRetrieveResponse(
    @Json(name = "appearanceDays")
    val appearanceDays: List<AppearanceDaysModel> = listOf(),
    @Json(name = "bossStoreId")
    val bossStoreId: String? = null,

    @Json(name = "categories")
    val categories: List<CategoriesModel> = listOf(),

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "distance")
    val distance: Int? = null,

    @Json(name = "favorite")
    val favorite: FavoriteModel? = null,

    @Json(name = "imageUrl")
    val imageUrl: String? = null,

    @Json(name = "introduction")
    val introduction: String? = null,

    @Json(name = "location")
    val location: LocationModel? = null,

    @Json(name = "menus")
    val menus: List<MenusModel>,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "openStatus")
    val openStatus: OpenStatusModel? = null,

    @Json(name = "snsUrl")
    val snsUrl: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
) : BaseResponse<BossStoreRetrieveResponse>() {
    fun toDto() = app.threedollars.domain.dto.BossStoreRetrieveDto(
        appearanceDays.toDto(),
        bossStoreId,
        categories.toDto(),
        createdAt,
        distance,
        favorite?.toDto(),
        imageUrl,
        introduction,
        location?.toDto(),
        menus.toDto(),
        name,
        openStatus?.toDto(),
        snsUrl,
        updatedAt
    )
}
