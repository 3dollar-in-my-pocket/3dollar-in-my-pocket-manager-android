package app.threedollars.manager.feature.home.preference

import app.threedollars.domain.dto.StorePreferenceDto

internal data class StorePreferenceState(
    val storeId: String = "",
    val retainLocationOnClose: Boolean = false,
    val autoOpenCloseControl: Boolean = false,
    val isLoaded: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String? = null,
) {
    val isToggleEnabled: Boolean get() = isLoaded

    fun withPreference(preference: StorePreferenceDto) = copy(
        retainLocationOnClose = preference.retainLocationOnClose,
        autoOpenCloseControl = preference.autoOpenCloseControl,
        isLoaded = true,
    )

    fun toggleRetainLocationOnClose() =
        if (isToggleEnabled) copy(retainLocationOnClose = !retainLocationOnClose) else this

    fun toggleAutoOpenCloseControl() =
        if (isToggleEnabled) copy(autoOpenCloseControl = !autoOpenCloseControl) else this

    fun toPreference() = StorePreferenceDto(
        retainLocationOnClose = retainLocationOnClose,
        autoOpenCloseControl = autoOpenCloseControl,
    )

    fun withError(message: String?) = copy(showError = true, errorMessage = message)

    fun dismissError() = copy(showError = false, errorMessage = null)
}
