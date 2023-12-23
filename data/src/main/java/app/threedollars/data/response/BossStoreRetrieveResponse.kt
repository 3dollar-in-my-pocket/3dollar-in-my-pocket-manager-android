package app.threedollars.data.response

import app.threedollars.data.BaseResponse
import app.threedollars.data.model.AccountNumbers
import app.threedollars.data.model.Address
import app.threedollars.data.model.AppearanceDaysModel
import app.threedollars.data.model.CategoriesModel
import app.threedollars.data.model.LocationModel
import app.threedollars.data.model.MenusModel
import app.threedollars.data.model.OpenStatusModel
import app.threedollars.data.model.toDto
import app.threedollars.domain.dto.AddressDto
import app.threedollars.domain.dto.BossStoreRetrieveDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossStoreRetrieveResponse(
    @Json(name = "bossStoreId")
    val bossStoreId: String? = null,
    @Json(name = "isOwner")
    val isOwner: Boolean? = true,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "location")
    val location: LocationModel? = null,
    @Json(name = "address")
    val address: Address? = Address(),
    @Json(name = "imageUrl")
    val imageUrl: String? = null,
    @Json(name = "introduction")
    val introduction: String? = null,
    @Json(name = "snsUrl")
    val snsUrl: String? = null,
    @Json(name = "menus")
    val menus: List<MenusModel>?,
    @Json(name = "appearanceDays")
    val appearanceDays: List<AppearanceDaysModel>? = listOf(),
    @Json(name = "categories")
    val categories: List<CategoriesModel>? = listOf(),
    @Json(name = "accountNumbers")
    val accountNumbers: List<AccountNumbers>? = listOf(),
    @Json(name = "openStatus")
    val openStatus: OpenStatusModel? = null,
    @Json(name = "distance")
    val distance: Int? = null,
    @Json(name = "createdAt")
    val createdAt: String? = null,
    @Json(name = "updatedAt")
    val updatedAt: String? = null,
) : BaseResponse<BossStoreRetrieveResponse>() {
    fun toDto() = BossStoreRetrieveDto(
        bossStoreId = bossStoreId,
        isOwner = isOwner ?: true,
        name = name,
        location = location?.toDto(),
        address = address?.toDto() ?: AddressDto(),
        imageUrl = imageUrl,
        introduction = introduction,
        snsUrl = snsUrl,
        menus = menus?.toDto() ?: listOf(),
        appearanceDays = appearanceDays?.toDto() ?: listOf(),
        categories = categories?.toDto() ?: listOf(),
        accountNumbersDto = accountNumbers?.map { it.toDto() } ?: listOf(),
        createdAt = createdAt,
        distance = distance,
        openStatus = openStatus?.toDto(),
        updatedAt = updatedAt,
    )
}
