package app.threedollars.domain.dto

data class AppStatusDto(
    val forceUpdate: AppForceUpdateDto = AppForceUpdateDto(),
)

data class AppForceUpdateDto(
    val enabled: Boolean = false,
    val title: String? = null,
    val message: String? = null,
    val linkUrl: String? = null,
)
