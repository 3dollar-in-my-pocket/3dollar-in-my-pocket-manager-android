package app.threedollars.common.ui.photo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PhotoViewerStateTest {

    // TH-242 TC7
    @Test
    fun `TH242_TC7_대표_사진_3장이면_1_3부터_표시한다`() {
        // Given
        val total = 3
        // When
        val position = PhotoViewerPosition.of(initialIndex = 0, total = total)
        // Then
        assertEquals(1, position.displayIndex)
        assertEquals(3, position.total)
    }

    // TH-242 TC11
    @Test
    fun `TH242_TC11_리뷰_사진_3장_중_두_번째를_누르면_2_3으로_열린다`() {
        // Given
        val tappedIndex = 1
        // When
        val position = PhotoViewerPosition.of(initialIndex = tappedIndex, total = 3)
        // Then
        assertEquals(2, position.displayIndex)
        assertEquals(3, position.total)
        assertTrue(position.isPreviousVisible)
        assertTrue(position.isNextVisible)
    }

    // TH-242 TC11
    @Test
    fun `TH242_TC11_핀치_확대는_1배에서_4배_사이로_제한된다`() {
        // Given
        val tooSmall = 0.5f
        val tooLarge = 6f
        // When
        val min = clampPhotoScale(tooSmall)
        val max = clampPhotoScale(tooLarge)
        // Then
        assertEquals(1f, min)
        assertEquals(4f, max)
        assertEquals(2.5f, clampPhotoScale(2.5f))
    }

    // TH-242 TC11
    @Test
    fun `TH242_TC11_확대하지_않으면_이동할_수_없고_확대하면_확대된_만큼만_이동한다`() {
        // Given
        val size = 100f
        // When
        val notZoomed = clampPhotoOffset(offset = 30f, scale = 1f, size = size)
        val zoomed = clampPhotoOffset(offset = 300f, scale = 2f, size = size)
        // Then
        assertEquals(0f, notZoomed)
        assertEquals(50f, zoomed)
    }

    // TH-242 TC12
    @Test
    fun `TH242_TC12_첫_장에서는_이전_버튼이_마지막_장에서는_다음_버튼이_숨겨진다`() {
        // Given
        val total = 3
        // When
        val first = PhotoViewerPosition(index = 0, total = total)
        val last = PhotoViewerPosition(index = 2, total = total)
        // Then
        assertFalse(first.isPreviousVisible)
        assertTrue(first.isNextVisible)
        assertTrue(last.isPreviousVisible)
        assertFalse(last.isNextVisible)
    }

    // TH-242 TC12
    @Test
    fun `TH242_TC12_사진이_1장이면_이전_다음_버튼이_모두_숨겨지고_범위를_벗어난_시작_위치는_보정된다`() {
        // Given
        val total = 1
        // When
        val position = PhotoViewerPosition.of(initialIndex = 5, total = total)
        // Then
        assertEquals(0, position.index)
        assertFalse(position.isPreviousVisible)
        assertFalse(position.isNextVisible)
    }
}
