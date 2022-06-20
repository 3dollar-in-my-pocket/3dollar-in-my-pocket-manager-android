package app.threedollars.data.store.response


import app.threedollars.data.store.model.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyStoreResponse(
    @Json(name = "appearanceDays")
    val appearanceDayModels: List<AppearanceDayModel>?,
    @Json(name = "bossStoreId")
    val bossStoreId: String?,
    @Json(name = "categories")
    val categories: List<CategoryModel>?,
    @Json(name = "contactsNumber")
    val contactsNumber: String?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "distance")
    val distance: Int?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "introduction")
    val introduction: String?,
    @Json(name = "location")
    val locationModel: LocationModel?,
    @Json(name = "menus")
    val menuModels: List<MenuModel>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "openStatus")
    val openStatusModel: OpenStatusModel?,
    @Json(name = "snsUrl")
    val snsUrl: String?,
    @Json(name = "updatedAt")
    val updatedAt: String?
)