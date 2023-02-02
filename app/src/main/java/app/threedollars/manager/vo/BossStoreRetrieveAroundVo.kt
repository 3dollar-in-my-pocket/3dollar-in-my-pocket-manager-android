package app.threedollars.manager.vo


data class BossStoreRetrieveAroundVo(
    val bossStoreId: String? = null,
    val categories: List<CategoriesVo> = listOf(),
    val createdAt: String? = null,
    val distance: Int? = null,
    val location: LocationVo? = null,
    val menus: List<MenusVo> = listOf(),
    val name: String? = null,
    val openStatus: OpenStatusVo,
    val totalFeedbacksCounts: Int? = null,
    val updatedAt: String? = null
)
