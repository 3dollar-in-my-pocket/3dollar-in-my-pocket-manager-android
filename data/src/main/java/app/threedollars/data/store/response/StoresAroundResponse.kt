package app.threedollars.data.store.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoresAroundResponse(
    @Json(name = "bossStoreId")
    val bossStoreId: String?,
    @Json(name = "categories")
    val categories: List<Category>?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "distance")
    val distance: Int?,
    @Json(name = "location")
    val location: Location?,
    @Json(name = "menus")
    val menus: List<Menu>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "openStatus")
    val openStatus: OpenStatus?,
    @Json(name = "totalFeedbacksCounts")
    val totalFeedbacksCounts: Int?,
    @Json(name = "updatedAt")
    val updatedAt: String?
)