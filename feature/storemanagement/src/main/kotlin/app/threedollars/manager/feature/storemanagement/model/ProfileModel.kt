package app.threedollars.manager.feature.storemanagement.model

data class ProfileModel(
    val images: List<String>,
    val name: String,
    val category: List<String>,
    val snsLink: String,
    val contactNumber: String,
)
