package app.threedollars.manager.feature.storemanagement.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountNumbersVoTest {

    // TH-524 TC13
    @Test
    fun `TH524_TC13_계좌가_등록돼_있으면_계좌정보_삭제를_노출한다`() {
        // Given
        val accountNumbers = listOf(
            AccountNumbersVo(
                bankVo = BankVo(key = "KB", description = "국민은행"),
                accountHolder = "홍길동",
                accountNumber = "1234567890",
            )
        )
        // When
        val hasAccount = accountNumbers.hasRegisteredAccount()
        // Then
        assertTrue(hasAccount)
    }

    // TH-524 TC13
    @Test
    fun `TH524_TC13_계좌가_없으면_계좌정보_삭제를_노출하지_않는다`() {
        // Given
        val accountNumbers = emptyList<AccountNumbersVo>()
        // When
        val hasAccount = accountNumbers.hasRegisteredAccount()
        // Then
        assertFalse(hasAccount)
    }

    // TH-524 TC14
    @Test
    fun `TH524_TC14_삭제로_계좌목록이_비면_가게정보에_빈_상태를_노출한다`() {
        // Given
        val accountNumbersAfterDelete = emptyList<AccountNumbersVo>()
        // When
        val showEmptyState = !accountNumbersAfterDelete.hasRegisteredAccount()
        // Then
        assertTrue(showEmptyState)
    }
}
