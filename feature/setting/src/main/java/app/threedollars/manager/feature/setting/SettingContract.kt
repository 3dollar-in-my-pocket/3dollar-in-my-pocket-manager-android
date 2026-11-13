package app.threedollars.manager.feature.setting

import app.threedollars.common.toDisplayErrorMessage
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
    val isPushUpdating: Boolean = false,
    val pushErrorMessage: String? = null,
) {
    val isActivitiesPushEnabled: Boolean get() = bossAccountInfo.settings.enableActivitiesPush

    val isPushToggleEnabled: Boolean get() = bossAccountInfo.bossId.isNotEmpty() && !isPushUpdating

    fun withActivitiesPush(enable: Boolean) = copy(
        bossAccountInfo = bossAccountInfo.copy(
            settings = bossAccountInfo.settings.copy(enableActivitiesPush = enable)
        )
    )

    fun startPushUpdate(enable: Boolean) = withActivitiesPush(enable).copy(isPushUpdating = true)

    fun finishPushUpdate() = copy(isPushUpdating = false)

    fun failPushUpdate(rollback: Boolean, message: String?) =
        withActivitiesPush(rollback).copy(isPushUpdating = false, pushErrorMessage = message ?: DEFAULT_PUSH_ERROR_MESSAGE)

    fun dismissPushError() = copy(pushErrorMessage = null)

    companion object {
        const val DEFAULT_PUSH_ERROR_MESSAGE = "푸시 알림 설정을 변경하지 못했어요. 잠시 후 다시 시도해 주세요."
        private const val CLIENT_ERROR_CODE = "700"

        fun pushErrorMessageOf(code: String?, errorMessage: String?): String? {
            if (code.isNullOrEmpty() || code == CLIENT_ERROR_CODE) return null
            return errorMessage.toDisplayErrorMessage()
        }
    }
}

enum class ScreenType {
    SETTING, FAQ
}