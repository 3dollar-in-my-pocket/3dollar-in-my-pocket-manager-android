package app.threedollars.data.request

import app.threedollars.di.NetworkModule
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test

class BossLogoutRequestTest {

    private val json = NetworkModule.provideJson()

    private fun BossLogoutRequest.toJsonObject(): JsonObject =
        json.encodeToJsonElement(BossLogoutRequest.serializer(), this).jsonObject

    // TH-424 TC1
    @Test
    fun `TH424_TC1_FCM_토큰이_있으면_logoutDevice에_플랫폼과_토큰을_담아_보낸다`() {
        // Given
        val pushToken = "fcm-token"
        // When
        val body = BossLogoutRequest.of(pushToken).toJsonObject()
        // Then
        val logoutDevice = body.getValue("logoutDevice").jsonObject
        assertEquals("FCM", logoutDevice.getValue("pushPlatform").jsonPrimitive.content)
        assertEquals("fcm-token", logoutDevice.getValue("pushToken").jsonPrimitive.content)
    }

    // TH-424 TC2
    @Test
    fun `TH424_TC2_FCM_토큰을_못_받으면_logoutDevice_없이_로그아웃한다`() {
        // Given
        val missingTokens = listOf(null, "", " ")
        // When
        val bodies = missingTokens.map { BossLogoutRequest.of(it).toJsonObject() }
        // Then
        bodies.forEach { body -> assertEquals(JsonNull, body.getValue("logoutDevice")) }
    }
}
