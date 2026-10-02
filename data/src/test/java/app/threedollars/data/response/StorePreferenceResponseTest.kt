package app.threedollars.data.response

import app.threedollars.data.request.StorePreferenceRequest
import app.threedollars.domain.dto.StorePreferenceDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StorePreferenceResponseTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-242 TC1
    @Test
    fun `TH242_TC1_가게_설정_응답이_두_토글_값으로_매핑된다`() {
        // Given
        val response = json.decodeFromString<StorePreferenceResponse>(fixture("storePreference.json"))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(StorePreferenceDto(retainLocationOnClose = true, autoOpenCloseControl = false), dto)
    }

    // TH-242 TC1
    @Test
    fun `TH242_TC1_설정_값이_없으면_OFF로_떨어진다`() {
        // Given
        val response = json.decodeFromString<StorePreferenceResponse>("{}")
        // When
        val dto = response.toDto()
        // Then
        assertFalse(dto.retainLocationOnClose)
        assertFalse(dto.autoOpenCloseControl)
    }

    // TH-242 TC3
    @Test
    fun `TH242_TC3_설정_변경_요청은_두_값을_항상_함께_보낸다`() {
        // Given
        val dto = StorePreferenceDto(retainLocationOnClose = true, autoOpenCloseControl = true)
        // When
        val body = json.parseToJsonElement(json.encodeToString(StorePreferenceRequest.from(dto))).jsonObject
        // Then
        assertEquals(setOf("retainLocationOnClose", "autoOpenCloseControl"), body.keys)
        assertTrue(body.getValue("retainLocationOnClose").jsonPrimitive.boolean)
        assertTrue(body.getValue("autoOpenCloseControl").jsonPrimitive.boolean)
    }
}
