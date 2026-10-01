package app.threedollars.data.response

import app.threedollars.data.request.BossStoreRequest
import app.threedollars.data.request.withRepresentativeImages
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BossStoreRepresentativeImagesTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-242 TC7
    @Test
    fun `TH242_TC7_대표_사진_목록이_순서대로_매핑된다`() {
        // Given
        val response = json.decodeFromString<BossStoreRetrieveResponse>(fixture("bossStoreRepresentativeImages.json"))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(
            listOf(
                "https://cdn.threedollars.co.kr/store/first.png",
                "https://cdn.threedollars.co.kr/store/second.png",
                "https://cdn.threedollars.co.kr/store/third.png",
            ),
            dto.representativeImages.map { it.imageUrl }
        )
        assertEquals(1080, dto.representativeImages.first().width)
    }

    // TH-242 TC7
    @Test
    fun `TH242_TC7_representativeImages가_없는_응답은_imageUrl_한_장으로_대체한다`() {
        // Given
        val raw = fixture("bossStoreRepresentativeImages.json")
        val legacy = json.parseToJsonElement(raw).jsonObject.toMutableMap().apply { remove("representativeImages") }
        val response = json.decodeFromString<BossStoreRetrieveResponse>(json.encodeToString(kotlinx.serialization.json.JsonObject(legacy)))
        // When
        val dto = response.toDto()
        // Then
        assertEquals(listOf("https://cdn.threedollars.co.kr/store/first.png"), dto.representativeImages.map { it.imageUrl })
    }

    // TH-242 TC10
    @Test
    fun `TH242_TC10_가게_수정_요청에_대표_사진_전체_목록이_imageUrl_객체로_담긴다`() {
        // Given
        val request = BossStoreRequest(name = "붕어빵 트럭")
        // When
        val body = json.parseToJsonElement(
            json.encodeToString(request.withRepresentativeImages(listOf("https://cdn/new.png", "https://cdn/old.png")))
        ).jsonObject
        // Then
        val images = body.getValue("representativeImages").jsonArray.map { it.jsonObject.getValue("imageUrl").jsonPrimitive.content }
        assertEquals(listOf("https://cdn/new.png", "https://cdn/old.png"), images)
    }

    @Test
    fun `회귀_대표_사진을_바꾸지_않으면_representativeImages는_null로_보내_기존_사진을_유지한다`() {
        // Given
        val request = BossStoreRequest(name = "붕어빵 트럭")
        // When
        val body = json.parseToJsonElement(json.encodeToString(request.withRepresentativeImages(null))).jsonObject
        // Then
        assertTrue(body.getValue("representativeImages") is JsonNull)
    }
}
