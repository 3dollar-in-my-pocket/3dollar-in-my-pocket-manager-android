package app.threedollars.manager.feature.storemanagement.components.storepost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostSectionRequestDto
import app.threedollars.domain.dto.StorePostSectionType
import app.threedollars.domain.usecase.DeleteStorePostUseCase
import app.threedollars.domain.usecase.GetStorePostListUseCase
import app.threedollars.domain.usecase.ImageUploadUseCase
import app.threedollars.domain.usecase.PatchStorePostUseCase
import app.threedollars.domain.usecase.PostStorePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
internal class StorePostViewModel @Inject constructor(
    private val getStorePostListUseCase: GetStorePostListUseCase,
    private val postStorePostUseCase: PostStorePostUseCase,
    private val patchStorePostUseCase: PatchStorePostUseCase,
    private val deleteStorePostUseCase: DeleteStorePostUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(StorePostState())
    val stateFlow: StateFlow<StorePostState> = _stateFlow.asStateFlow()

    private val _uploadStateFlow = MutableStateFlow(UploadPostState())
    val uploadStateFlow: StateFlow<UploadPostState> = _uploadStateFlow.asStateFlow()

    private val _uploadFinishedFlow = MutableSharedFlow<Unit>()
    val uploadFinishedFlow = _uploadFinishedFlow.asSharedFlow()

    private var skipNextTabReload = false
    private var scrollToTopAfterLoad = false

    fun onTabEntered(storeId: String) {
        if (skipNextTabReload) {
            skipNextTabReload = false
            return
        }
        loadFirstPage(storeId)
    }

    fun loadFirstPage(storeId: String) {
        if (storeId.isEmpty()) return
        _stateFlow.update { it.copy(storeId = storeId, isLoading = true) }
        viewModelScope.launch {
            val result = getStorePostListUseCase(storeId = storeId, size = STORE_POST_PAGE_SIZE)
            when {
                result is Resource.Success && result.data != null -> {
                    val data = result.data!!
                    val scrollToTop = scrollToTopAfterLoad
                    scrollToTopAfterLoad = false
                    _stateFlow.update {
                        it.withFirstPage(
                            page = data.contents,
                            nextCursor = data.cursor?.nextCursor,
                            hasMore = data.cursor?.hasMore ?: false,
                        ).copy(scrollToTopRequested = scrollToTop)
                    }
                }

                else -> _stateFlow.update {
                    it.copy(isLoading = false, errorMessage = result.errorMessage.orDefault())
                }
            }
        }
    }

    fun loadNextPage() {
        val state = _stateFlow.value
        if (!state.canLoadMore) return
        _stateFlow.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            val result = getStorePostListUseCase(
                storeId = state.storeId,
                size = STORE_POST_PAGE_SIZE,
                cursor = state.nextCursor,
            )
            when {
                result is Resource.Success && result.data != null -> {
                    val data = result.data!!
                    _stateFlow.update {
                        it.withNextPage(
                            page = data.contents,
                            nextCursor = data.cursor?.nextCursor,
                            hasMore = data.cursor?.hasMore ?: false,
                        )
                    }
                }

                else -> _stateFlow.update {
                    it.copy(isLoadingMore = false, errorMessage = result.errorMessage.orDefault())
                }
            }
        }
    }

    fun requestDelete(postId: String) {
        _stateFlow.update { it.copy(deleteTargetPostId = postId) }
    }

    fun cancelDelete() {
        _stateFlow.update { it.copy(deleteTargetPostId = null) }
    }

    fun confirmDelete() {
        val state = _stateFlow.value
        val postId = state.deleteTargetPostId ?: return
        _stateFlow.update { it.copy(isLoading = true, deleteTargetPostId = null) }
        viewModelScope.launch {
            val result = deleteStorePostUseCase(storeId = state.storeId, postId = postId)
            if (result is Resource.Success) {
                _stateFlow.update { it.withPostRemoved(postId) }
            } else {
                _stateFlow.update { it.copy(isLoading = false, errorMessage = result.errorMessage.orDefault()) }
            }
        }
    }

    fun consumeScrollToTop() {
        _stateFlow.update { it.copy(scrollToTopRequested = false) }
    }

    fun clearError() {
        _stateFlow.update { it.copy(errorMessage = null) }
    }

    fun startUpload(post: StorePostDto? = null) {
        skipNextTabReload = true
        _uploadStateFlow.value = if (post == null) UploadPostState() else UploadPostState.forEdit(post)
    }

    fun updateBody(body: String) {
        _uploadStateFlow.update { it.withBody(body) }
    }

    fun addPhotos(photos: List<UploadPhoto.Local>) {
        _uploadStateFlow.update { it.withPhotosAdded(photos) }
    }

    fun removePhoto(index: Int) {
        _uploadStateFlow.update { it.withPhotoRemoved(index) }
    }

    fun clearUploadError() {
        _uploadStateFlow.update { it.copy(errorMessage = null) }
    }

    fun save(localBodies: (UploadPhoto.Local) -> RequestBody?) {
        val uploadState = _uploadStateFlow.value
        if (!uploadState.isSaveEnabled) return
        val storeId = _stateFlow.value.storeId
        _uploadStateFlow.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val uploadedUrls = uploadLocalPhotos(uploadState.photos, localBodies)
            if (uploadedUrls == null) {
                _uploadStateFlow.update { it.copy(isSaving = false, errorMessage = "사진 업로드에 실패했습니다.") }
                return@launch
            }
            val sections = buildSections(uploadState.photos, uploadedUrls)
            val editingPostId = uploadState.editingPostId
            if (editingPostId == null) {
                val result = postStorePostUseCase(storeId = storeId, body = uploadState.body, sections = sections)
                if (result is Resource.Success) {
                    _uploadStateFlow.update { it.copy(isSaving = false) }
                    scrollToTopAfterLoad = true
                    loadFirstPage(storeId)
                    _uploadFinishedFlow.emit(Unit)
                } else {
                    _uploadStateFlow.update { it.copy(isSaving = false, errorMessage = result.errorMessage.orDefault()) }
                }
            } else {
                val result = patchStorePostUseCase(
                    storeId = storeId,
                    postId = editingPostId,
                    body = uploadState.body,
                    sections = sections,
                )
                if (result is Resource.Success && result.data != null) {
                    _uploadStateFlow.update { it.copy(isSaving = false) }
                    _stateFlow.update { it.withPostReplaced(result.data!!) }
                    _uploadFinishedFlow.emit(Unit)
                } else {
                    _uploadStateFlow.update { it.copy(isSaving = false, errorMessage = result.errorMessage.orDefault()) }
                }
            }
        }
    }

    private suspend fun uploadLocalPhotos(
        photos: List<UploadPhoto>,
        localBodies: (UploadPhoto.Local) -> RequestBody?,
    ): List<String>? {
        val locals = photos.filterIsInstance<UploadPhoto.Local>()
        if (locals.isEmpty()) return emptyList()
        val bodies = locals.map { localBodies(it) ?: return null }
        val result = imageUploadUseCase.postImageUploadBulk(STORE_POST_IMAGE_FILE_TYPE, bodies).first()
        val urls = result.data?.map { it.imageUrl.orEmpty() } ?: return null
        if (urls.size != locals.size || urls.any { it.isEmpty() }) return null
        return urls
    }

    private fun buildSections(photos: List<UploadPhoto>, uploadedUrls: List<String>): List<StorePostSectionRequestDto> {
        var localIndex = 0
        return photos.map { photo ->
            when (photo) {
                is UploadPhoto.Remote -> StorePostSectionRequestDto(
                    sectionType = StorePostSectionType.IMAGE,
                    url = photo.url,
                    ratio = photo.ratio,
                )

                is UploadPhoto.Local -> StorePostSectionRequestDto(
                    sectionType = StorePostSectionType.IMAGE,
                    url = uploadedUrls[localIndex++],
                    ratio = photo.ratio,
                )
            }
        }
    }

    private fun String?.orDefault() = this?.takeIf { it.isNotBlank() } ?: "요청에 실패했습니다. 잠시 후 다시 시도해주세요."

    companion object {
        private const val STORE_POST_IMAGE_FILE_TYPE = "STORE_POST_IMAGE"
    }
}
