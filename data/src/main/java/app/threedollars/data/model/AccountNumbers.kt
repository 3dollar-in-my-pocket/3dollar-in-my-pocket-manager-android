package app.threedollars.data.model

import app.threedollars.domain.dto.AccountNumbersDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountNumbers(
    @Json(name = "bank")
    val bank: Bank = Bank(),
    @Json(name = "accountHolder")
    val accountHolder: String = "",
    @Json(name = "accountNumber")
    val accountNumber: String = "",
    @Json(name = "description")
    val description: String? = null,
) {
    fun toDto() = AccountNumbersDto(
        bankDto = bank.toDto(),
        accountHolder = accountHolder,
        accountNumber = accountNumber,
        description = description,
    )
}
