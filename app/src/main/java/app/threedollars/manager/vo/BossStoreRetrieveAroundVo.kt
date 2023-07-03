package app.threedollars.manager.vo


data class BossStoreRetrieveAroundVo(
    val bossStoreId: String,
    val categories: List<CategoriesVo> = listOf(),
    val createdAt: String,
    val distance: Int,
    val location: LocationVo,
    val menus: List<MenusVo> = listOf(),
    val name: String,
    val openStatus: OpenStatusVo,
    val totalFeedbacksCounts: Int,
    val updatedAt: String
)
