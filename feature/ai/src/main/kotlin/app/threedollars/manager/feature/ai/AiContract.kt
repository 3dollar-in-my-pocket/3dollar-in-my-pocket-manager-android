package app.threedollars.manager.feature.ai

internal data class AiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val currentDate: String = "",
    val recommendationText: String = "",
    val bossStoreId: String = ""
)
