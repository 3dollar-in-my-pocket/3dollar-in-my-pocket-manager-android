package app.threedollars.manager.feature.setting

import app.threedollars.domain.dto.BossAccountInfoDto
import app.threedollars.domain.dto.FaqDto

/**
 * UI State that represents SettingScreen
 **/
data class SettingState(
    val bossAccountInfo: BossAccountInfoDto = BossAccountInfoDto(),
    val faqList: List<FaqDto> = listOf(),
    val screenType: ScreenType = ScreenType.SETTING,
    val isSuccess: Boolean = false,
)

enum class ScreenType {
    SETTING, FAQ
}