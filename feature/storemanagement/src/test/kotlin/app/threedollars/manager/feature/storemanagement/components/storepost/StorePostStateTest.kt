package app.threedollars.manager.feature.storemanagement.components.storepost

import app.threedollars.domain.dto.StorePostDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StorePostStateTest {

    private fun post(id: String, body: String = "body$id") = StorePostDto(postId = id, body = body)

    // TH-198 TC4
    @Test
    fun `TH198_TC4_hasMore가_false면_추가_조회하지_않는다`() {
        // Given
        val state = StorePostState().withFirstPage(page = listOf(post("1")), nextCursor = null, hasMore = false)
        // When
        val canLoadMore = state.canLoadMore
        // Then
        assertFalse(canLoadMore)
    }

    // TH-198 TC3
    @Test
    fun `TH198_TC3_hasMore가_true고_커서가_있으면_다음_페이지를_기존_목록_뒤에_이어_붙인다`() {
        // Given
        val state = StorePostState().withFirstPage(page = listOf(post("1"), post("2")), nextCursor = "c1", hasMore = true)
        assertTrue(state.canLoadMore)
        // When
        val next = state.withNextPage(page = listOf(post("3")), nextCursor = null, hasMore = false)
        // Then
        assertEquals(listOf("1", "2", "3"), next.posts.map { it.postId })
        assertFalse(next.canLoadMore)
    }

    // TH-198 TC16
    @Test
    fun `TH198_TC16_마지막_소식을_삭제하면_목록이_빈_상태가_된다`() {
        // Given
        val state = StorePostState().withFirstPage(page = listOf(post("1")), nextCursor = null, hasMore = false)
        // When
        val removed = state.withPostRemoved("1")
        // Then
        assertTrue(removed.isEmpty)
        assertEquals(null, removed.deleteTargetPostId)
    }

    // TH-198 TC14
    @Test
    fun `TH198_TC14_수정된_소식은_같은_위치에서_내용만_갱신된다`() {
        // Given
        val state = StorePostState().withFirstPage(page = listOf(post("1"), post("2"), post("3")), nextCursor = null, hasMore = false)
        // When
        val replaced = state.withPostReplaced(post("2", body = "edited"))
        // Then
        assertEquals(listOf("1", "2", "3"), replaced.posts.map { it.postId })
        assertEquals("edited", replaced.posts[1].body)
    }

    // TH-198 TC1
    @Test
    fun `TH198_TC1_첫_페이지가_0건이면_빈_상태로_판정한다`() {
        // Given
        val initial = StorePostState()
        assertFalse(initial.isEmpty)
        // When
        val loaded = initial.withFirstPage(page = emptyList(), nextCursor = null, hasMore = false)
        // Then
        assertTrue(loaded.isEmpty)
    }
}
