package app.threedollars.data.response

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppStatusResponseTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-897 TC1
    @Test
    fun `TH897_TC1_강제업데이트_응답이_enabled_title_message_linkUrl로_매핑된다`() {
        // Given
        val response = json.decodeFromString<AppStatusResponse>(fixture("appStatusForceUpdate.json"))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(true, dto.forceUpdate.enabled)
        assertEquals("업데이트 안내", dto.forceUpdate.title)
        assertEquals("원활한 사용을 위해 앱을 최신 버전으로 업데이트해주세요", dto.forceUpdate.message)
        assertEquals("https://play.google.com/store/apps/details?id=app.threedollars.manager", dto.forceUpdate.linkUrl)
    }

    // TH-897 TC2
    @Test
    fun `TH897_TC2_title_message_linkUrl이_없으면_null로_매핑된다`() {
        // Given
        val response = json.decodeFromString<AppStatusResponse>(fixture("appStatusForceUpdateNoText.json"))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(true, dto.forceUpdate.enabled)
        assertNull(dto.forceUpdate.title)
        assertNull(dto.forceUpdate.message)
        assertNull(dto.forceUpdate.linkUrl)
    }

    // TH-897 TC6
    @Test
    fun `TH897_TC6_강제업데이트_비대상_응답은_enabled_false로_매핑된다`() {
        // Given
        val response = json.decodeFromString<AppStatusResponse>(fixture("appStatusNormal.json"))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(false, dto.forceUpdate.enabled)
    }
}
