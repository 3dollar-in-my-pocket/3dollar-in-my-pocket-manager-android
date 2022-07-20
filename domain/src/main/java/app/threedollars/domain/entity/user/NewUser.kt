package app.threedollars.domain.entity.user

data class NewUser(
    val bossName: String? = null,
    val businessNumber: String? = null,
    val certificationPhotoUrl: String? = null,
    val contactsNumber: String? = null,
    val socialType: String? = null,
    val storeCategoriesIds: List<String>? = null,
    val storeName: String? = null,
)
