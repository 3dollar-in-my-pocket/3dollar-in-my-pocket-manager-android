package app.threedollars.manager.feature.storemanagement.components.profile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RepresentativePhotoStateTest {

    private fun urls(count: Int) = (1..count).map { "https://cdn/image$it.png" }

    // TH-242 TC8
    @Test
    fun `TH242_TC8_사진이_9장이면_1장만_더_추가된다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(9))
        // When
        val added = state.add(listOf("content://new1", "content://new2"))
        // Then
        assertEquals(1, state.remainingCount)
        assertEquals(10, added.count)
        assertEquals("content://new1", added.photos.first().model)
        assertFalse(added.canAdd)
    }

    // TH-242 TC8
    @Test
    fun `TH242_TC8_사진이_10장이면_더_추가할_수_없다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(10))
        // When
        val added = state.add(listOf("content://new1"))
        // Then
        assertFalse(state.canAdd)
        assertEquals(0, state.remainingCount)
        assertEquals(state, added)
    }

    // TH-242 TC9
    @Test
    fun `TH242_TC9_사진이_1장이면_삭제할_수_없다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(1))
        // When
        val removed = state.remove(0)
        // Then
        assertFalse(state.canDelete)
        assertEquals(state, removed)
    }

    // TH-242 TC9
    @Test
    fun `TH242_TC9_사진이_2장이면_삭제할_수_있고_삭제_후_1장이면_삭제_버튼이_사라진다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(2))
        // When
        val removed = state.remove(0)
        // Then
        assertTrue(state.canDelete)
        assertEquals(listOf("https://cdn/image2.png"), removed.photos.map { it.model })
        assertFalse(removed.canDelete)
    }

    // TH-242 TC10
    @Test
    fun `TH242_TC10_새로_추가한_사진은_맨_앞에_들어가고_새_사진만_업로드_대상이다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(2))
        // When
        val added = state.add(listOf("content://a", "content://b"))
        // Then
        assertEquals(listOf("content://a", "content://b", "https://cdn/image1.png", "https://cdn/image2.png"), added.photos.map { it.model })
        assertEquals(listOf("content://a", "content://b"), added.uploadTargets.map { it.uri })
    }

    // TH-242 TC10
    @Test
    fun `TH242_TC10_업로드된_URL을_새_사진_자리에_채워_전체_목록을_만든다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(3))
            .add(listOf("content://a"))
            .remove(2)
        // When
        val imageUrls = state.resolveImageUrls(listOf("https://cdn/uploaded-a.png"))
        // Then
        assertEquals(listOf("https://cdn/uploaded-a.png", "https://cdn/image1.png", "https://cdn/image3.png"), imageUrls)
        assertNull(state.resolveImageUrls(listOf()))
    }

    // TH-242 TC10
    @Test
    fun `TH242_TC10_이미_목록에_있는_사진을_다시_골라도_중복으로_추가되지_않는다`() {
        // Given
        val state = RepresentativePhotoState.from(urls(1)).add(listOf("content://a"))
        // When
        val added = state.add(listOf("content://a", "content://a", "content://b"))
        // Then
        assertEquals(listOf("content://b", "content://a", "https://cdn/image1.png"), added.photos.map { it.model })
    }

    // TH-242 TC10
    @Test
    fun `TH242_TC10_사진을_삭제만_해도_변경으로_판단하고_추가_삭제가_없으면_변경이_아니다`() {
        // Given
        val original = urls(3)
        val state = RepresentativePhotoState.from(original)
        // When
        val removed = state.remove(1)
        // Then
        assertFalse(state.isChangedFrom(original))
        assertTrue(removed.isChangedFrom(original))
        assertEquals(listOf("https://cdn/image1.png", "https://cdn/image3.png"), removed.resolveImageUrls(listOf()))
    }

    @Test
    fun `회귀_대표_정보_저장_시_바꾸지_않은_이름_SNS_카테고리는_보내지_않고_연락처는_기존_값을_유지한다`() {
        // Given
        val original = ProfileEditForm(
            name = "붕어빵 트럭",
            snsUrl = "https://instagram.com/store",
            categoryIds = listOf("BUNGEOPPANG"),
            contactNumber = "010-1234-5678",
            photos = RepresentativePhotoState.from(urls(2)),
        )
        // When
        val patch = original.copy(photos = original.photos.add(listOf("content://a"))).toPatch(original)
        // Then
        assertNull(patch.name)
        assertNull(patch.snsUrl)
        assertNull(patch.categoriesIds)
        assertEquals("010-1234-5678", patch.contactNumber)
        assertTrue(patch.isPhotoChanged)
        assertTrue(patch.hasChanges)
    }

    @Test
    fun `회귀_대표_정보에서_이름만_바꾸면_이름만_변경_대상이다`() {
        // Given
        val original = ProfileEditForm(
            name = "붕어빵 트럭",
            snsUrl = "",
            categoryIds = listOf("BUNGEOPPANG"),
            contactNumber = "",
            photos = RepresentativePhotoState.from(urls(1)),
        )
        // When
        val patch = original.copy(name = "호떡 트럭").toPatch(original)
        // Then
        assertEquals("호떡 트럭", patch.name)
        assertNull(patch.snsUrl)
        assertNull(patch.categoriesIds)
        assertFalse(patch.isPhotoChanged)
    }
}
