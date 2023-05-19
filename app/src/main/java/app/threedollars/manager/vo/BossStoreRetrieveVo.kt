package app.threedollars.manager.vo


data class BossStoreRetrieveVo(
    val appearanceDays: List<AppearanceDaysVo> = listOf(),
    val bossStoreId: String,
    val categories: List<CategoriesVo> = listOf(),
    val createdAt: String,
    val distance: Int,
    val favorite: FavoriteVo,
    val imageUrl: String,
    val introduction: String,
    val location: LocationVo,
    val menus: List<MenusVo> = listOf(),
    val name: String,
    val openStatus: OpenStatusVo,
    val snsUrl: String,
    val updatedAt: String
)
