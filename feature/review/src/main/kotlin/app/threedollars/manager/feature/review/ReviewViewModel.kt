package app.threedollars.manager.feature.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import app.threedollars.common.ext.toStringDefault
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.DeleteStoreCommentPresetUseCase
import app.threedollars.domain.usecase.DeleteStoreReviewCommentUseCase
import app.threedollars.domain.usecase.GetStoreCommentPresetListUseCase
import app.threedollars.domain.usecase.GetStoreReviewDetailUseCase
import app.threedollars.domain.usecase.GetStoreReviewPagingUseCase
import app.threedollars.domain.usecase.PatchStoreCommentPresetUseCase
import app.threedollars.domain.usecase.PostStoreCommentPresetUseCase
import app.threedollars.domain.usecase.PostStoreReviewCommentUseCase
import app.threedollars.domain.usecase.PostStoreReviewReportUseCase
import app.threedollars.domain.usecase.PutStickersReplaceUseCase
import app.threedollars.manager.feature.review.ScreenType.LOADING
import app.threedollars.manager.feature.review.ScreenType.REVIEW_DETAIL
import app.threedollars.manager.feature.review.model.ReviewVo
import app.threedollars.manager.feature.review.model.dtoToVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class ReviewViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val getStoreReviewPagingUseCase: GetStoreReviewPagingUseCase,
    private val getStoreReviewDetailUseCase: GetStoreReviewDetailUseCase,
    private val postStoreReviewReportUseCase: PostStoreReviewReportUseCase,
    private val postStoreReviewCommentUseCase: PostStoreReviewCommentUseCase,
    private val deleteStoreReviewCommentUseCase: DeleteStoreReviewCommentUseCase,
    private val postStoreCommentPresetUseCase: PostStoreCommentPresetUseCase,
    private val deleteStoreCommentPresetUseCase: DeleteStoreCommentPresetUseCase,
    private val patchStoreCommentPresetUseCase: PatchStoreCommentPresetUseCase,
    private val getStoreCommentPresetListUseCase: GetStoreCommentPresetListUseCase,
    private val putStickersReplaceUseCase: PutStickersReplaceUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<ReviewState> =
        MutableStateFlow(ReviewState())

    val stateFlow: StateFlow<ReviewState> = _stateFlow.asStateFlow()

    private val _storeReviewPaging = MutableStateFlow<Flow<PagingData<ReviewVo>>>(emptyFlow())

    val storeReviewPaging = _storeReviewPaging.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    private val _toastFlow = MutableSharedFlow<String?>()
    val toastFlow = _toastFlow.asSharedFlow()

    init {
        val reviewId = savedStateHandle.get<String?>("reviewId")
        reviewId?.let {
            updateScreenType(LOADING)
            getStoreReviewDetail(reviewId = reviewId)
        }
    }

    private fun getReviewPaging() {
        _storeReviewPaging.value = getStoreReviewPagingUseCase(
            storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
            sort = _stateFlow.value.reviewFilterType.name,
        ).map {
            it.map { dto -> dto.dtoToVo(storeName = _stateFlow.value.bossStoreRetrieve.storeName) }
        }.cachedIn(viewModelScope)
    }

    fun updateScreenType(screenType: ScreenType) {
        _stateFlow.update { it.copy(screenType = screenType) }
    }

    fun updateDialogType(dialogType: DialogType) {
        _stateFlow.update { it.copy(dialogType = dialogType) }
    }

    fun updateReviewFilterType(reviewFilterType: ReviewFilterType) {
        _stateFlow.update { it.copy(reviewFilterType = reviewFilterType) }
        getReviewPaging()
    }

    fun updateEditPreset(
        presetId: String,
        presetText: String,
    ) {
        _stateFlow.update { state ->
            state.copy(
                dialogType = DialogType.PRESET_EDIT_DIALOG,
                selectEditPresetId = presetId,
                selectEditPresetText = presetText
            )
        }
    }

    fun getBossStoreRetrieveMe() {
        viewModelScope.launch {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect {
                if (it.code.toString() == "200") {
                    it.data?.let { data ->
                        _stateFlow.update { state ->
                            state.copy(bossStoreRetrieve = data.dtoToVo())
                        }
                    }
                    getReviewPaging()
                }
            }
        }
    }

    fun getStoreReviewDetail(reviewId: String) {
        viewModelScope.launch {
            getStoreReviewDetailUseCase(reviewId = reviewId).collect {
                if (it.code.toString() == "200") {
                    it.data?.let { data ->
                        _stateFlow.update { state ->
                            state.copy(selectedReview = data.dtoToVo(storeName = _stateFlow.value.bossStoreRetrieve.storeName))
                        }
                    }
                }
                updateScreenType(REVIEW_DETAIL)
            }

        }
    }

    fun reportStoreReview(
        reasonDetail: String,
        onReportComplete: () -> Unit
    ) {
        viewModelScope.launch {
            postStoreReviewReportUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                reviewId = _stateFlow.value.selectedReview.reviewId,
                reasonDetail = reasonDetail
            ).collect {
                if (it.code.toString() == "200") {
                    _toastFlow.emit("신고 완료!")
                }
                _stateFlow.update { state ->
                    state.copy(dialogType = DialogType.NONE)
                }
                onReportComplete.invoke()
            }
        }
    }

    fun postStoreReviewComment(reviewComment: String) {
        viewModelScope.launch {
            val result = postStoreReviewCommentUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                reviewId = _stateFlow.value.selectedReview.reviewId,
                reviewComment = reviewComment
            )

            if (result.code == "200") {
                _toastFlow.emit("답글 등록 완료!")
                getStoreReviewDetail(
                    reviewId = _stateFlow.value.selectedReview.reviewId
                )
            }
        }
    }

    fun deleteStoreReviewComment() {
        viewModelScope.launch {
            val result = deleteStoreReviewCommentUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                reviewId = _stateFlow.value.selectedReview.reviewId,
                commentId = _stateFlow.value.selectedReview.comment?.commentId.toStringDefault()
            )

            result.data?.let {
                _toastFlow.emit("삭제 완료!")
                getStoreReviewDetail(
                    reviewId = _stateFlow.value.selectedReview.reviewId
                )
            }
        }
    }

    fun postStoreCommentPreset(body: String) {
        viewModelScope.launch {
            val result = postStoreCommentPresetUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                body = body
            )
            if (result.code == "200") {
                _toastFlow.emit("등록 완료!")
                getStoreCommentPresets()
                updateDialogType(DialogType.PRESET_DIALOG)
            }
        }
    }

    fun deleteStoreCommentPreset(presetId: String) {
        viewModelScope.launch {
            val result = deleteStoreCommentPresetUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                presetId = presetId
            )

            result.data?.let {
                _toastFlow.emit("삭제 완료!")
                getStoreCommentPresets()
            }
        }
    }

    fun patchStoreCommentPreset(presetId: String, body: String) {
        viewModelScope.launch {
            val result = patchStoreCommentPresetUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                presetId = presetId,
                body = body
            )
            if (result.code == "200") {
                _toastFlow.emit("수정 완료!")
                getStoreCommentPresets()
                updateDialogType(DialogType.PRESET_DIALOG)
            }
        }
    }

    fun getStoreCommentPresets() {
        viewModelScope.launch {
            val result = getStoreCommentPresetListUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId
            )
            if (result.code == "200") {
                _stateFlow.update { state ->
                    state.copy(
                        dialogType = DialogType.PRESET_DIALOG,
                        commentPresets = result.data?.contents?.map { it.dtoToVo() } ?: listOf()
                    )
                }
            }
        }
    }

    fun putStickersReplace(
        reviewId: String,
        stickers: String,
        isDetail: Boolean,
    ) {
        viewModelScope.launch {
            val code = putStickersReplaceUseCase(
                storeId = _stateFlow.value.bossStoreRetrieve.bossStoreId,
                reviewId = reviewId,
                stickers = stickers
            ).code

            if (code == "200") {
                if (isDetail) {
                    getStoreReviewDetail(reviewId = reviewId)
                } else {
                    _storeReviewPaging.value = _storeReviewPaging.value.map { pagingData ->
                        pagingData.map { review ->
                            if (review.reviewId == reviewId) {
                                review.copy(
                                    sticker = review.sticker.copy(
                                        reactedByMe = !review.sticker.reactedByMe,
                                        count = if (!review.sticker.reactedByMe) review.sticker.count + 1 else review.sticker.count - 1
                                    )
                                )
                            } else {
                                review
                            }
                        }
                    }
                }
            }
        }
    }
}
