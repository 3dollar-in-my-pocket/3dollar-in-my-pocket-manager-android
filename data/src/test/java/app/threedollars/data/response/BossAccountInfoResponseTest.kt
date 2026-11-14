package app.threedollars.data.response

import app.threedollars.domain.dto.BossAccountInfoDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class BossAccountInfoResponseTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-1405 TC4
    @Test
    fun `TH1405_TC4_계정_응답의_푸시_설정과_방해금지_시간이_매핑된다`() {
        // Given
        val response = json.decodeFromString(BossAccountInfoResponse.serializer(), fixture("bossAccountMe.json"))
        // When
        val settings = response.toDto().settings
        // Then
        assertFalse(settings.enableActivitiesPush)
        assertEquals(BossAccountInfoDto.PushQuietHours(startTime = "22:00", endTime = "08:00"), settings.pushQuietHours)
    }

    // TH-1405 TC4
    @Test
    fun `TH1405_TC4_방해금지_시간이_없거나_비어있으면_null로_떨어진다`() {
        // Given
        val missing = """{"bossId":"boss-1","settings":{"enableActivitiesPush":true}}"""
        val blank = """{"bossId":"boss-1","settings":{"enableActivitiesPush":true,"pushQuietHours":{"startTime":"","endTime":null}}}"""
        // When
        val missingDto = json.decodeFromString(BossAccountInfoResponse.serializer(), missing).toDto()
        val blankDto = json.decodeFromString(BossAccountInfoResponse.serializer(), blank).toDto()
        // Then
        assertNull(missingDto.settings.pushQuietHours)
        assertNull(blankDto.settings.pushQuietHours)
    }
}
