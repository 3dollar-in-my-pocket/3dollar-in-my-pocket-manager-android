package app.threedollars.data.request

/**
 * 가게 수정(PATCH) 요청에서 null 필드는 서버가 변경하지 않는다.
 * 빈 배열은 해당 목록을 비운다는 뜻이므로, 호출자가 명시한 경우에만 보낸다.
 */
internal fun accountNumbersRequestOf(
    accountNumber: String?,
    accountHolder: String?,
    accountBank: String?,
): List<AccountNumberRequest>? =
    if (accountNumber != null && accountHolder != null && accountBank != null) {
        listOf(
            AccountNumberRequest(
                accountNumber = accountNumber,
                accountHolder = accountHolder,
                bank = accountBank,
            )
        )
    } else {
        null
    }

internal fun contactNumbersRequestOf(contactNumber: String?): List<ContactNumberRequest>? =
    when {
        contactNumber == null -> null
        contactNumber.isEmpty() -> emptyList()
        else -> listOf(ContactNumberRequest(number = contactNumber, description = "string"))
    }

internal fun deleteAccountNumbersRequest(): BossStoreRequest =
    BossStoreRequest(accountNumbers = emptyList())
