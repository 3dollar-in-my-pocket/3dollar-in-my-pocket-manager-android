package app.threedollars.manager.feature.storemanagement.components.storepost

import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostSectionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UploadPostStateTest {

    private fun localPhotos(count: Int) = (1..count).map { UploadPhoto.Local(uri = "content://$it", ratio = 1.0) }

    // TH-198 TC7
    @Test
    fun `TH198_TC7_작성_화면_초기_상태는_사진_0장_본문_빈값_저장_비활성이다`() {
        // Given
        val state = UploadPostState()
        // When
        val enabled = state.isSaveEnabled
        // Then
        assertEquals(0, state.photos.size)
        assertEquals("", state.body)
        assertFalse(enabled)
        assertFalse(state.isDirty)
    }

    // TH-198 TC10
    @Test
    fun `TH198_TC10_본문을_입력하면_저장이_활성화되고_500자를_넘는_입력은_잘린다`() {
        // Given
        val state = UploadPostState()
        // When
        val typed = state.withBody("소식")
        val overflow = state.withBody("a".repeat(600))
        // Then
        assertTrue(typed.isSaveEnabled)
        assertEquals(UPLOAD_POST_MAX_BODY_LENGTH, overflow.body.length)
    }

    // TH-198 TC11
    @Test
    fun `TH198_TC11_본문을_모두_지우면_저장이_비활성화된다`() {
        // Given
        val state = UploadPostState().withBody("소식")
        // When
        val cleared = state.withBody("")
        // Then
        assertFalse(cleared.isSaveEnabled)
    }

    // TH-198 TC8
    @Test
    fun `TH198_TC8_사진은_최대_10장까지만_추가되고_남은_개수가_갱신된다`() {
        // Given
        val state = UploadPostState().withPhotosAdded(localPhotos(8))
        assertEquals(2, state.remainingPhotoCount)
        // When
        val overflow = state.withPhotosAdded(localPhotos(5))
        // Then
        assertEquals(UPLOAD_POST_MAX_PHOTO, overflow.photos.size)
        assertEquals(0, overflow.remainingPhotoCount)
    }

    // TH-198 TC9
    @Test
    fun `TH198_TC9_썸네일을_삭제하면_해당_사진만_빠지고_카운트가_준다`() {
        // Given
        val state = UploadPostState().withPhotosAdded(localPhotos(3))
        // When
        val removed = state.withPhotoRemoved(1)
        // Then
        assertEquals(listOf("content://1", "content://3"), removed.photos.map { (it as UploadPhoto.Local).uri })
        assertEquals(8, removed.remainingPhotoCount)
    }

    // TH-198 TC18
    @Test
    fun `TH198_TC18_저장_처리_중에는_저장_버튼이_비활성이다`() {
        // Given
        val state = UploadPostState().withBody("소식")
        // When
        val saving = state.copy(isSaving = true)
        // Then
        assertFalse(saving.isSaveEnabled)
    }

    // TH-198 TC14
    @Test
    fun `TH198_TC14_수정_진입_시_기존_본문과_이미지_섹션만_프리필되고_저장이_활성이다`() {
        // Given
        val post = StorePostDto(
            postId = "p1",
            body = "기존 본문",
            sections = listOf(
                StorePostDto.Section(sectionType = StorePostSectionType.IMAGE, url = "https://img/1", ratio = 1.5),
                StorePostDto.Section(sectionType = StorePostSectionType.UNKNOWN, url = "https://img/2", ratio = 1.0),
            ),
        )
        // When
        val state = UploadPostState.forEdit(post)
        // Then
        assertTrue(state.isEditMode)
        assertEquals("기존 본문", state.body)
        assertEquals(listOf(UploadPhoto.Remote(url = "https://img/1", ratio = 1.5)), state.photos)
        assertTrue(state.isSaveEnabled)
    }
}
