package app.threedollars.manager.feature.home.preference

import app.threedollars.domain.dto.StorePreferenceDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StorePreferenceStateTest {

    // TH-242 TC1
    @Test
    fun `TH242_TC1_서버_설정값으로_두_토글이_초기화되고_조회_전에는_토글이_비활성이다`() {
        // Given
        val initial = StorePreferenceState(storeId = "store-1")
        // When
        val loaded = initial.withPreference(StorePreferenceDto(retainLocationOnClose = true, autoOpenCloseControl = false))
        // Then
        assertFalse(initial.isToggleEnabled)
        assertEquals(initial, initial.toggleRetainLocationOnClose())
        assertTrue(loaded.isToggleEnabled)
        assertTrue(loaded.retainLocationOnClose)
        assertFalse(loaded.autoOpenCloseControl)
    }

    // TH-242 TC2
    @Test
    fun `TH242_TC2_위치_노출을_ON으로_바꾸면_retainLocationOnClose_true로_저장한다`() {
        // Given
        val state = StorePreferenceState(storeId = "store-1")
            .withPreference(StorePreferenceDto(retainLocationOnClose = false, autoOpenCloseControl = false))
        // When
        val request = state.toggleRetainLocationOnClose().toPreference()
        // Then
        assertEquals(StorePreferenceDto(retainLocationOnClose = true, autoOpenCloseControl = false), request)
    }

    // TH-242 TC3
    @Test
    fun `TH242_TC3_자동_변경을_ON으로_바꾸면_위치_노출_값과_함께_저장한다`() {
        // Given
        val state = StorePreferenceState(storeId = "store-1")
            .withPreference(StorePreferenceDto(retainLocationOnClose = true, autoOpenCloseControl = false))
        // When
        val request = state.toggleAutoOpenCloseControl().toPreference()
        // Then
        assertEquals(StorePreferenceDto(retainLocationOnClose = true, autoOpenCloseControl = true), request)
    }

    // TH-242 TC3
    @Test
    fun `TH242_TC3_저장_실패해도_토글은_되돌리지_않고_에러만_노출한다`() {
        // Given
        val toggled = StorePreferenceState(storeId = "store-1")
            .withPreference(StorePreferenceDto())
            .toggleAutoOpenCloseControl()
        // When
        val failed = toggled.withError("에러")
        // Then
        assertTrue(failed.autoOpenCloseControl)
        assertTrue(failed.showError)
        assertFalse(failed.dismissError().showError)
    }
}
