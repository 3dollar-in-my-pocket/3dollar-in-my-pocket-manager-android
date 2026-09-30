package app.threedollars.data.response

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StoreMessageResponseTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-391 TC3
    @Test
    fun `TH391_TC3_메세지_목록_응답이_본문_시각_커서_정책으로_매핑된다`() {
        // Given
        val response = json.decodeFromString<StoreMessageListResponse>(fixture("storeMessageList.json"))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(listOf("message-2", "message-1"), dto.contents.map { it.messageId })
        assertEquals("여러분 제가 왔습니다!\n여기는 망원 합정 사이 지에스 바로 앞!", dto.contents.first().body)
        assertEquals("2024-10-24T20:56:00", dto.contents.first().createdAt)
        assertEquals("cursor-1", dto.cursor?.nextCursor)
        assertTrue(dto.cursor?.hasMore == true)
        assertFalse(dto.policy.canSendNow)
        assertEquals("2024-10-25T20:56:00", dto.policy.nextAvailableSendDateTime)
    }

    // TH-391 TC16
    @Test
    fun `TH391_TC16_전송_응답이_새_메세지와_정책으로_매핑된다`() {
        // Given
        val raw = """
            {
              "message": { "messageId": "message-3", "body": "새 메세지", "isOwner": true, "createdAt": "2024-10-25T21:00:00", "updatedAt": "2024-10-25T21:00:00" },
              "policy": { "maxQuota": 1, "remainingQuota": 0, "canSendNow": false, "nextAvailableSendDateTime": "2024-10-26T21:00:00" }
            }
        """.trimIndent()
        // When
        val dto = json.decodeFromString<StoreMessageCreateResponse>(raw).toDto()
        // Then
        assertEquals("message-3", dto.message.messageId)
        assertEquals("새 메세지", dto.message.body)
        assertFalse(dto.policy.canSendNow)
        assertEquals("2024-10-26T21:00:00", dto.policy.nextAvailableSendDateTime)
    }
}
