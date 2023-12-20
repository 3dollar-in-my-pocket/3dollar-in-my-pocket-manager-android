package app.threedollars.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountNumberRequest(
    @Json(name = "accountNumber")
    val accountNumber: String,
    @Json(name = "accountHolder")
    val accountHolder: String,
    @Json(name = "bank")
    val bank: String,
)