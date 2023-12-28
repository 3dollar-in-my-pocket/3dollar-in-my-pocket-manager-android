package app.threedollars.domain.dto

data class AccountNumbersDto(
    val bankDto: BankDto = BankDto(),
    val accountHolder: String = "",
    val accountNumber: String = "",
    val description: String? = null,
)
