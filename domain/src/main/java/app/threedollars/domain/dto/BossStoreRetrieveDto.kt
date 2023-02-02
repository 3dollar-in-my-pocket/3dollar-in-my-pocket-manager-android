package app.threedollars.domain.dto


data class BossStoreRetrieveDto(
    val appearanceDays: List<AppearanceDaysDto> = listOf(),
    val bossStoreId: String? = null,
    val categories: List<CategoriesDto> = listOf(),
    val createdAt: String? = null,
    val distance: Int? = null,
    val favorite: FavoriteDto? = null,
    val imageUrl: String? = null,
    val introduction: String? = null,
    val location: LocationDto? = null,
    val menus: List<MenusDto>,
    val name: String? = null,
    val openStatus: OpenStatusDto? = null,
    val snsUrl: String? = null,
    val updatedAt: String? = null
)
