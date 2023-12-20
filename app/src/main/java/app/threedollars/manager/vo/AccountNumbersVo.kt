package app.threedollars.manager.vo

data class AccountNumbersVo(
    val bankVo: BankVo = BankVo(),
    val accountHolder: String = "",
    val accountNumber: String = "",
    val description: String? = null,
)
