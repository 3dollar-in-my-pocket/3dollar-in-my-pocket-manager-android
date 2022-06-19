package app.threedollars.domain.repository

data class SignUpUser(
    val bossName: String,
    val businessNumber: String,
    val certificationPhotoUrl: String,
    val contactsNumber: String,
    val socialType: String,
    val storeCategoriesIds: List<String>,
    val storeName: String,
    val token: String
)