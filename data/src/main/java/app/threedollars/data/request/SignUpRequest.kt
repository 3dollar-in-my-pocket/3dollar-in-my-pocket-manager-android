package app.threedollars.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    @Json(name = "bossName")
    val bossName: String? = null,
    @Json(name = "businessNumber")
    val businessNumber: String? = null,
    @Json(name = "certificationPhotoUrl")
    val certificationPhotoUrl: String? = null,
    @Json(name = "socialType")
    val socialType: String? = null,
    @Json(name = "storeCategoriesIds")
    val storeCategoriesIds: List<String>? = null,
    @Json(name = "storeName")
    val storeName: String? = null,
    @Json(name = "token")
    val token: String? = null
)