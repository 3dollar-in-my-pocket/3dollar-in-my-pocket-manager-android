package app.threedollars.domain.dto


data class BossStoreRetrieveAroundDto(
    val bossStoreId: String? = null,
    val categories: List<CategoriesDto> = listOf(),
    val createdAt: String? = null,
    val distance: Int? = null,
    val location: LocationDto? = null,
    val menus: List<MenusDto> = listOf(),
    val name: String? = null,
    val openStatus: OpenStatusDto,
    val totalFeedbacksCounts: Int? = null,
    val updatedAt: String? = null
)
