package app.threedollars.manager.feature.storemanagement.components.profile

internal const val MAX_REPRESENTATIVE_PHOTO_COUNT = 10

internal sealed interface RepresentativePhoto {
    val model: String

    data class Uploaded(val imageUrl: String) : RepresentativePhoto {
        override val model: String get() = imageUrl
    }

    data class Local(val uri: String) : RepresentativePhoto {
        override val model: String get() = uri
    }
}

internal data class RepresentativePhotoState(
    val photos: List<RepresentativePhoto> = listOf(),
) {
    val count: Int get() = photos.size

    val remainingCount: Int get() = (MAX_REPRESENTATIVE_PHOTO_COUNT - count).coerceAtLeast(0)

    val canAdd: Boolean get() = remainingCount > 0

    val canDelete: Boolean get() = count > 1

    val uploadTargets: List<RepresentativePhoto.Local> get() = photos.filterIsInstance<RepresentativePhoto.Local>()

    fun add(uris: List<String>): RepresentativePhotoState {
        val existing = photos.map { it.model }.toSet()
        val newPhotos = uris.distinct()
            .filterNot { it in existing }
            .take(remainingCount)
            .map { RepresentativePhoto.Local(it) }
        return if (newPhotos.isEmpty()) this else copy(photos = newPhotos + photos)
    }

    fun remove(index: Int): RepresentativePhotoState =
        if (!canDelete || index !in photos.indices) this else copy(photos = photos.filterIndexed { i, _ -> i != index })

    fun isChangedFrom(originalImageUrls: List<String>): Boolean =
        uploadTargets.isNotEmpty() || photos.map { it.model } != originalImageUrls

    fun resolveImageUrls(uploadedImageUrls: List<String>): List<String>? {
        if (uploadedImageUrls.size != uploadTargets.size) return null
        val uploaded = uploadedImageUrls.iterator()
        return photos.map { photo ->
            when (photo) {
                is RepresentativePhoto.Uploaded -> photo.imageUrl
                is RepresentativePhoto.Local -> uploaded.next()
            }
        }
    }

    companion object {
        fun from(imageUrls: List<String>) = RepresentativePhotoState(imageUrls.map { RepresentativePhoto.Uploaded(it) })
    }
}

internal data class ProfileEditForm(
    val name: String,
    val snsUrl: String,
    val categoryIds: List<String>,
    val contactNumber: String,
    val photos: RepresentativePhotoState,
)

internal data class ProfilePatch(
    val name: String?,
    val snsUrl: String?,
    val categoriesIds: List<String>?,
    val contactNumber: String,
    val isPhotoChanged: Boolean,
)

internal fun ProfileEditForm.toPatch(original: ProfileEditForm) = ProfilePatch(
    name = name.takeIf { it.isNotEmpty() && it != original.name },
    snsUrl = snsUrl.takeIf { it != original.snsUrl },
    categoriesIds = categoryIds.takeIf { it.isNotEmpty() && it != original.categoryIds },
    contactNumber = contactNumber,
    isPhotoChanged = photos.isChangedFrom(original.photos.photos.map { it.model }),
)

internal val ProfilePatch.hasChanges: Boolean
    get() = name != null || snsUrl != null || categoriesIds != null || isPhotoChanged
