package app.threedollars.data.request

import app.threedollars.di.NetworkModule
import app.threedollars.domain.dto.BossAccountInfoDto
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BossAccountSettingsRequestTest {

    private val json = NetworkModule.provideJson()

    private fun BossAccountSettingsRequest.toJsonObject(): JsonObject =
        json.encodeToJsonElement(BossAccountSettingsRequest.serializer(), this).jsonObject

    // TH-1405 TC1
    @Test
    fun `TH1405_TC1_푸시를_끄면_enableActivitiesPush_false로_보낸다`() {
        // Given
        val request = BossAccountSettingsRequest.of(enableActivitiesPush = false, pushQuietHours = null)
        // When
        val body = request.toJsonObject()
        // Then
        assertFalse(body.getValue("enableActivitiesPush").jsonPrimitive.boolean)
    }

    // TH-1405 TC2
    @Test
    fun `TH1405_TC2_방해금지_시간이_있으면_그대로_유지해서_보낸다`() {
        // Given
        val quietHours = BossAccountInfoDto.PushQuietHours(startTime = "22:00", endTime = "08:00")
        // When
        val body = BossAccountSettingsRequest.of(enableActivitiesPush = true, pushQuietHours = quietHours).toJsonObject()
        // Then
        val pushQuietHours = body.getValue("pushQuietHours").jsonObject
        assertTrue(body.getValue("enableActivitiesPush").jsonPrimitive.boolean)
        assertTrue(pushQuietHours.getValue("enabled").jsonPrimitive.boolean)
        assertEquals("22:00", pushQuietHours.getValue("startTime").jsonPrimitive.content)
        assertEquals("08:00", pushQuietHours.getValue("endTime").jsonPrimitive.content)
    }

    // TH-1405 TC3
    @Test
    fun `TH1405_TC3_방해금지_시간이_없으면_enabled_false로_보낸다`() {
        // Given
        val request = BossAccountSettingsRequest.of(enableActivitiesPush = true, pushQuietHours = null)
        // When
        val pushQuietHours = request.toJsonObject().getValue("pushQuietHours").jsonObject
        // Then
        assertFalse(pushQuietHours.getValue("enabled").jsonPrimitive.boolean)
        assertEquals(JsonNull, pushQuietHours["startTime"] ?: JsonNull)
        assertEquals(JsonNull, pushQuietHours["endTime"] ?: JsonNull)
    }
}
