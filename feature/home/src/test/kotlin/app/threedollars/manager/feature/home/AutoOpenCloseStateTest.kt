package app.threedollars.manager.feature.home

import app.threedollars.domain.dto.StorePreferenceDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoOpenCloseStateTest {

    // TH-242 TC4
    @Test
    fun `TH242_TC4_자동_변경_ON_상태에서_셔터를_내리면_영업_종료_대신_안내_팝업을_띄운다`() {
        // Given
        val state = AutoOpenCloseState().withPreference(StorePreferenceDto(autoOpenCloseControl = true))
        // When
        val action = state.shutterAction
        val next = state.onShutterClick()
        // Then
        assertEquals(ShutterAction.SHOW_AUTO_OPEN_CLOSE_ALERT, action)
        assertTrue(next.showAlert)
    }

    // TH-242 TC5
    @Test
    fun `TH242_TC5_안내_팝업을_닫으면_팝업만_사라지고_자동_변경_설정은_유지된다`() {
        // Given
        val state = AutoOpenCloseState(autoOpenCloseControl = true).onShutterClick()
        // When
        val next = state.dismissAlert()
        // Then
        assertFalse(next.showAlert)
        assertTrue(next.autoOpenCloseControl)
    }

    // TH-242 TC6
    @Test
    fun `TH242_TC6_자동_변경_OFF_상태에서_셔터를_내리면_기존처럼_영업을_종료한다`() {
        // Given
        val state = AutoOpenCloseState().withPreference(StorePreferenceDto(autoOpenCloseControl = false))
        // When
        val action = state.shutterAction
        val next = state.onShutterClick()
        // Then
        assertEquals(ShutterAction.CLOSE_STORE, action)
        assertFalse(next.showAlert)
    }

    // TH-242 TC6
    @Test
    fun `TH242_TC6_홈_재진입_시_다시_조회한_설정이_OFF면_셔터가_영업_종료로_돌아온다`() {
        // Given
        val state = AutoOpenCloseState(autoOpenCloseControl = true)
        // When
        val refreshed = state.withPreference(StorePreferenceDto(autoOpenCloseControl = false))
        // Then
        assertEquals(ShutterAction.CLOSE_STORE, refreshed.shutterAction)
    }
}
