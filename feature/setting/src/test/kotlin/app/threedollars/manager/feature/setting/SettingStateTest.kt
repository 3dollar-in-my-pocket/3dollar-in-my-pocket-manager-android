package app.threedollars.manager.feature.setting

import app.threedollars.domain.dto.BossAccountInfoDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingStateTest {

    private fun loadedState(enableActivitiesPush: Boolean) = SettingState(
        bossAccountInfo = BossAccountInfoDto(
            bossId = "boss-1",
            settings = BossAccountInfoDto.Settings(enableActivitiesPush = enableActivitiesPush)
        )
    )

    // TH-1405 TC5
    @Test
    fun `TH1405_TC5_토글하면_즉시_반영되고_요청이_끝날때까지_토글이_잠긴다`() {
        // Given
        val state = loadedState(enableActivitiesPush = true)
        // When
        val updating = state.startPushUpdate(enable = false)
        // Then
        assertFalse(updating.isActivitiesPushEnabled)
        assertFalse(updating.isPushToggleEnabled)
        assertTrue(updating.finishPushUpdate().isPushToggleEnabled)
        assertFalse(updating.finishPushUpdate().isActivitiesPushEnabled)
    }

    // TH-1405 TC5
    @Test
    fun `TH1405_TC5_요청이_실패하면_이전_값으로_되돌리고_에러를_띄운다`() {
        // Given
        val updating = loadedState(enableActivitiesPush = true).startPushUpdate(enable = false)
        // When
        val failed = updating.failPushUpdate(rollback = true, message = null)
        // Then
        assertTrue(failed.isActivitiesPushEnabled)
        assertTrue(failed.isPushToggleEnabled)
        assertEquals(SettingState.DEFAULT_PUSH_ERROR_MESSAGE, failed.pushErrorMessage)
        assertNull(failed.dismissPushError().pushErrorMessage)
    }

    // TH-1405 TC6
    @Test
    fun `TH1405_TC6_계정_정보를_받기_전에는_토글이_잠긴다`() {
        // Given
        val state = SettingState()
        // When
        val isEnabled = state.isPushToggleEnabled
        // Then
        assertFalse(isEnabled)
    }
}
