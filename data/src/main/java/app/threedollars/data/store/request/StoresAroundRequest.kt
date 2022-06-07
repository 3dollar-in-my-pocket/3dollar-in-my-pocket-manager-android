package app.threedollars.data.store.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoresAroundRequest(
    @Json(name = "categoryId")
    val categoryId: String? = null,
    @Json(name = "distanceKm")
    val distanceKm: Double? = null,
    @Json(name = "mapLatitude")
    val mapLatitude: String,
    @Json(name = "mapLongitude")
    val mapLongitude: String,
    @Json(name = "orderType")
    val orderType: String? = null,
    @Json(name = "size")
    val size: Int? = null
)