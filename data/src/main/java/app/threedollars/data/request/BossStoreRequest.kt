package app.threedollars.data.request


import app.threedollars.data.model.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossStoreRequest(
    @Json(name = "appearanceDays")
    val appearanceDays: List<AppearanceDaysRequestModel>? = null,
    @Json(name = "categoriesIds")
    val categoriesIds: List<String>? = null,
    @Json(name = "imageUrl")
    val imageUrl: String? = null,
    @Json(name = "introduction")
    val introduction: String? = null,
    @Json(name = "menus")
    val menus: List<MenusModel>? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "snsUrl")
    val snsUrl: String? = null,
    @Json(name = "accountNumbers")
    val accountNumbers: List<AccountNumberRequest>? = null
)