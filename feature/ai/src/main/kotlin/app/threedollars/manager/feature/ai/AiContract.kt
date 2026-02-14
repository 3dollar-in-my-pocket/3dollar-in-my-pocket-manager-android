package app.threedollars.manager.feature.ai

internal data class AiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val userName: String = "",
    val currentDate: String = "",
    val recommendationText: String = "",
    val bossStoreId: String = ""
)
