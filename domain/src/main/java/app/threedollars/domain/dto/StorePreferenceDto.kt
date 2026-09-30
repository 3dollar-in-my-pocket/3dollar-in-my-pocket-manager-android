package app.threedollars.domain.dto

data class StorePreferenceDto(
    val retainLocationOnClose: Boolean = false,
    val autoOpenCloseControl: Boolean = false,
)
