package app.threedollars.manager.feature.storemanagement.components.storepost

import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostSectionType

internal const val STORE_POST_PAGE_SIZE = 20
internal const val UPLOAD_POST_MAX_PHOTO = 10
internal const val UPLOAD_POST_MAX_BODY_LENGTH = 500

internal data class StorePostState(
    val storeId: String = "",
    val posts: List<StorePostDto> = listOf(),
    val isInitialLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
    val deleteTargetPostId: String? = null,
    val errorMessage: String? = null,
) {
    val isEmpty: Boolean get() = isInitialLoaded && posts.isEmpty()

    val canLoadMore: Boolean get() = isInitialLoaded && hasMore && !nextCursor.isNullOrEmpty() && !isLoading && !isLoadingMore

    fun withFirstPage(page: List<StorePostDto>, nextCursor: String?, hasMore: Boolean) = copy(
        posts = page,
        isInitialLoaded = true,
        isLoading = false,
        isLoadingMore = false,
        nextCursor = nextCursor,
        hasMore = hasMore,
    )

    fun withNextPage(page: List<StorePostDto>, nextCursor: String?, hasMore: Boolean) = copy(
        posts = posts + page,
        isLoadingMore = false,
        nextCursor = nextCursor,
        hasMore = hasMore,
    )

    fun withPostRemoved(postId: String) = copy(
        posts = posts.filterNot { it.postId == postId },
        deleteTargetPostId = null,
        isLoading = false,
    )

    fun withPostReplaced(post: StorePostDto) = copy(
        posts = posts.map { if (it.postId == post.postId) post else it },
    )
}

internal sealed interface UploadPhoto {
    val ratio: Double

    data class Remote(val url: String, override val ratio: Double) : UploadPhoto
    data class Local(val uri: String, override val ratio: Double) : UploadPhoto
}

internal data class UploadPostState(
    val editingPostId: String? = null,
    val body: String = "",
    val photos: List<UploadPhoto> = listOf(),
    val originalBody: String = "",
    val originalPhotos: List<UploadPhoto> = listOf(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
) {
    val isEditMode: Boolean get() = editingPostId != null

    val isSaveEnabled: Boolean get() = body.isNotBlank() && !isSaving

    val remainingPhotoCount: Int get() = UPLOAD_POST_MAX_PHOTO - photos.size

    val isDirty: Boolean get() = body != originalBody || photos != originalPhotos

    fun withBody(input: String) = copy(body = input.take(UPLOAD_POST_MAX_BODY_LENGTH))

    fun withPhotosAdded(added: List<UploadPhoto>) = copy(photos = (photos + added).take(UPLOAD_POST_MAX_PHOTO))

    fun withPhotoRemoved(index: Int) = copy(photos = photos.filterIndexed { i, _ -> i != index })

    companion object {
        fun forEdit(post: StorePostDto): UploadPostState {
            val photos = post.sections
                .filter { it.sectionType == StorePostSectionType.IMAGE }
                .map { UploadPhoto.Remote(url = it.url, ratio = it.ratio) }
            return UploadPostState(
                editingPostId = post.postId,
                body = post.body,
                photos = photos,
                originalBody = post.body,
                originalPhotos = photos,
            )
        }
    }
}
