package app.threedollars.manager.feature.storemanagement.components.profile

import okhttp3.RequestBody

internal data class ProfileSaveRequest(
    val bossStoreId: String,
    val patch: ProfilePatch,
    val photos: RepresentativePhotoState,
    val newPhotoBodies: List<RequestBody>,
)
