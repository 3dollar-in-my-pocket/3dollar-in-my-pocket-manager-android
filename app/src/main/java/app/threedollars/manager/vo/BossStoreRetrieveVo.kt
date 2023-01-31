package app.threedollars.manager.vo


data class BossStoreRetrieveVo(
    val appearanceDays: List<AppearanceDaysVo> = listOf(),
    val bossStoreId: String? = null,
    val categories: List<CategoriesVo> = listOf(),
    val createdAt: String? = null,
    val distance: Int? = null,
    val favorite: FavoriteVo? = null,
    val imageUrl: String? = null,
    val introduction: String? = null,
    val location: LocationVo? = null,
    val menus: List<MenusVo>,
    val name: String? = null,
    val openStatus: OpenStatusVo? = null,
    val snsUrl: String? = null,
    val updatedAt: String? = null
)
