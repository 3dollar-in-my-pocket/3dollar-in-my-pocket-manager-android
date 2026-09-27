package app.threedollars.data.response

import app.threedollars.domain.dto.StorePostSectionType
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StorePostResponseTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-198 TC2
    @Test
    fun `TH198_TC2_목록_응답이_소식_카드에_필요한_필드로_매핑된다`() {
        // Given
        val response = json.decodeFromString<StorePostListResponse>(fixture("storePostList.json"))
        // When
        val dto = response.toDto()
        // Then
        val first = dto.contents.first()
        assertEquals("post-1", first.postId)
        assertEquals("붕어빵 트럭", first.store.storeName)
        assertEquals("https://cdn.threedollars.co.kr/category/bungeoppang.png", first.store.categories.first().imageUrl)
        assertEquals(3, first.stickers.first().count)
        assertEquals("2026-09-26T10:00:00", first.createdAt)
        assertEquals(true, dto.cursor?.hasMore)
        assertEquals("post-2", dto.cursor?.nextCursor)
    }

    // TH-198 TC5
    @Test
    fun `TH198_TC5_섹션이_없는_소식은_빈_섹션_목록으로_매핑된다`() {
        // Given
        val response = json.decodeFromString<StorePostListResponse>(fixture("storePostList.json"))
        // When
        val dto = response.toDto()
        // Then
        assertTrue(dto.contents[1].sections.isEmpty())
    }

    // TH-198 TC6
    @Test
    fun `TH198_TC6_이미지_섹션의_ratio가_유지되고_알_수_없는_sectionType은_UNKNOWN으로_떨어진다`() {
        // Given
        val response = json.decodeFromString<StorePostListResponse>(fixture("storePostList.json"))
        // When
        val sections = response.toDto().contents.first().sections
        // Then
        assertEquals(StorePostSectionType.IMAGE, sections[0].sectionType)
        assertEquals(1.5, sections[0].ratio, 0.0)
        assertEquals(StorePostSectionType.UNKNOWN, sections[1].sectionType)
    }
}
