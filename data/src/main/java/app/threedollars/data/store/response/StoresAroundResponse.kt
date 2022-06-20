package app.threedollars.data.store.response


import app.threedollars.data.store.model.CategoryModel
import app.threedollars.data.store.model.LocationModel
import app.threedollars.data.store.model.MenuModel
import app.threedollars.data.store.model.OpenStatusModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoresAroundResponse(
    @Json(name = "bossStoreId")
    val bossStoreId: String?,
    @Json(name = "categories")
    val categories: List<CategoryModel>?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "distance")
    val distance: Int?,
    @Json(name = "location")
    val location: LocationModel?,
    @Json(name = "menus")
    val menus: List<MenuModel>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "openStatus")
    val openStatusModel: OpenStatusModel?,
    @Json(name = "totalFeedbacksCounts")
    val totalFeedbacksCounts: Int?,
    @Json(name = "updatedAt")
    val updatedAt: String?
)