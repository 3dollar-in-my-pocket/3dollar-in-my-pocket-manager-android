package app.threedollars.data.store.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyAccountResponse(
    @Json(name = "bossId")
    val bossId: String? = "",
    @Json(name = "businessNumber")
    val businessNumber: String? = "",
    @Json(name = "createdAt")
    val createdAt: String? = "",
    @Json(name = "isSetupNotification")
    val isSetupNotification: Boolean? = false,
    @Json(name = "name")
    val name: String? = "",
    @Json(name = "socialType")
    val socialType: String? = "",
    @Json(name = "updatedAt")
    val updatedAt: String? = ""
)