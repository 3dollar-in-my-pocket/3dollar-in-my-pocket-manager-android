package app.threedollars.domain.dto

data class BossStoreRetrieveDto(
    val bossStoreId: String? = null,
    val isOwner: Boolean = true,
    val name: String? = null,
    val location: LocationDto? = null,
    val address: AddressDto = AddressDto(),
    val imageUrl: String? = null,
    val introduction: String? = null,
    val snsUrl: String? = null,
    val menus: List<MenusDto> = listOf(),
    val appearanceDays: List<AppearanceDaysDto> = listOf(),
    val categories: List<CategoriesDto> = listOf(),
    val accountNumbersDto: List<AccountNumbersDto> = listOf(),
    val createdAt: String? = null,
    val distance: Int? = null,
    val openStatus: OpenStatusDto? = null,
    val updatedAt: String? = null,
)
