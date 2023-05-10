package app.threedollars.manager.storeManagement.viewModel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.threedollars.common.BaseViewModel
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.FeedbackUseCase
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.FeedbackFullVo
import app.threedollars.manager.vo.FeedbackTypesVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val feedbackUseCase: FeedbackUseCase
) : BaseViewModel() {

    init {
        getFeedbackType()
        getFeedbackFull()
    }

    private var bossStoreId: String? = null

    private val _feedbackFullList = MutableStateFlow<List<FeedbackFullVo>>(listOf())
    val feedbackFullList: StateFlow<List<FeedbackFullVo>> get() = _feedbackFullList

    private val _feedbackTypeList = MutableStateFlow<List<FeedbackTypesVo>>(listOf())
    val feedbackTypeList: StateFlow<List<FeedbackTypesVo>> get() = _feedbackTypeList

    val feedbackSpecificDto by lazy {
        feedbackUseCase.getFeedbackSpecific(bossStoreId.toString()).cachedIn(viewModelScope)
    }

    private fun getFeedbackFull() {
        viewModelScope.launch {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect {
                if (it.code == "200") {
                    feedbackUseCase.getFeedbackFull("BOSS_STORE", it.data?.bossStoreId.toString()).collect { feedbackFullDto ->
                        bossStoreId = it.data?.bossStoreId
                        val feedbackFullVoList = feedbackFullDto.data?.map { dto -> dto.dtoToVo() }
                        if (feedbackFullVoList != null) {
                            _feedbackFullList.emit(feedbackFullVoList)
                        }
                    }
                }
            }

        }
    }

    private fun getFeedbackType() {
        viewModelScope.launch {
            feedbackUseCase.getFeedbackTypes("BOSS_STORE").collect {
                val feedbackTypesVo = it.data?.map { dto -> dto.dtoToVo() }

                if (feedbackTypesVo != null) {
                    _feedbackTypeList.emit(feedbackTypesVo)
                }
            }
        }
    }
}