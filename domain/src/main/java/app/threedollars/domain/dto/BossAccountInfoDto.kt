package app.threedollars.domain.dto

data class BossAccountInfoDto(
    val bossId: String = "",
    val businessNumber: String = "",
    val createdAt: String = "",
    val name: String = "",
    val socialType: String = "",
    val settings: Settings = Settings(),
    val updatedAt: String = ""
) {
    data class Settings(
        val enableActivitiesPush: Boolean = false,
        val enableSalesAIRecommendation: Boolean = false
    )
}
