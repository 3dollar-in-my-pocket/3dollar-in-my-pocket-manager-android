package app.threedollars.data.request

import app.threedollars.di.NetworkModule
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BossStorePatchRequestFactoryTest {

    private val json = NetworkModule.provideJson()

    private fun BossStoreRequest.toJsonObject(): JsonObject =
        json.encodeToJsonElement(BossStoreRequest.serializer(), this).jsonObject

    private fun JsonObject.isAbsentOrNull(key: String): Boolean =
        this[key] == null || this[key] == JsonNull

    // TH-524 TC14
    @Test
    fun `TH524_TC14_계좌삭제_요청은_accountNumbers만_빈배열이고_다른_필드는_보내지_않는다`() {
        // Given
        val request = deleteAccountNumbersRequest()
        // When
        val body = request.toJsonObject()
        // Then
        assertEquals(JsonArray(emptyList()), body["accountNumbers"])
        listOf(
            "appearanceDays", "categoriesIds", "imageUrl", "introduction",
            "menus", "name", "snsUrl", "contactsNumbers",
        ).forEach { key ->
            assertTrue("$key 는 null 이어야 서버가 변경하지 않는다", body.isAbsentOrNull(key))
        }
    }

    // TH-524 TC14
    @Test
    fun `TH524_TC14_연락처를_전달하지_않으면_contactsNumbers가_null이라_연락처가_유지된다`() {
        // Given
        val contactNumber: String? = null
        // When
        val contactNumbers = contactNumbersRequestOf(contactNumber)
        // Then
        assertEquals(null, contactNumbers)
    }

    // TH-524 TC14
    @Test
    fun `TH524_TC14_계좌수정_요청은_accountNumbers_한개만_보내고_연락처는_보내지_않는다`() {
        // Given
        val request = BossStoreRequest(
            accountNumbers = accountNumbersRequestOf("1234567890", "홍길동", "KB"),
            contactNumbers = contactNumbersRequestOf(null),
        )
        // When
        val body = request.toJsonObject()
        // Then
        val account = body.getValue("accountNumbers").jsonArray.single().jsonObject
        assertEquals("1234567890", account.getValue("accountNumber").jsonPrimitive.content)
        assertEquals("홍길동", account.getValue("accountHolder").jsonPrimitive.content)
        assertEquals("KB", account.getValue("bank").jsonPrimitive.content)
        assertTrue(body.isAbsentOrNull("contactsNumbers"))
    }

    // TH-524 TC14
    @Test
    fun `TH524_TC14_계좌_필드가_하나라도_없으면_accountNumbers를_보내지_않는다`() {
        // Given
        val accountBank: String? = null
        // When
        val accountNumbers = accountNumbersRequestOf("1234567890", "홍길동", accountBank)
        // Then
        assertEquals(null, accountNumbers)
    }

    @Test
    fun `회귀_연락처를_빈_문자열로_보내면_contactsNumbers가_빈배열이라_연락처가_삭제된다`() {
        // Given
        val contactNumber = ""
        // When
        val contactNumbers = contactNumbersRequestOf(contactNumber)
        // Then
        assertEquals(emptyList<ContactNumberRequest>(), contactNumbers)
    }

    @Test
    fun `회귀_연락처를_입력하면_contactsNumbers에_한개가_담긴다`() {
        // Given
        val contactNumber = "010-1234-5678"
        // When
        val contactNumbers = contactNumbersRequestOf(contactNumber)
        // Then
        assertEquals(listOf(ContactNumberRequest(number = "010-1234-5678", description = "string")), contactNumbers)
    }
}
