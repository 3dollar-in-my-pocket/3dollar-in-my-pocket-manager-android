package app.threedollars.manager.feature.ai

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.domain.usecase.BossAccountUseCase
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.StoreRecommendationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class AiViewModel @Inject constructor(
    private val bossAccountUseCase: BossAccountUseCase,
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val storeRecommendationUseCase: StoreRecommendationUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _stateFlow: MutableStateFlow<AiState> = MutableStateFlow(AiState())

    val stateFlow: StateFlow<AiState> = _stateFlow.asStateFlow()

    private var hasLoadedData: Boolean
        get() = savedStateHandle.get<Boolean>(KEY_HAS_LOADED_DATA) ?: false
        set(value) = savedStateHandle.set(KEY_HAS_LOADED_DATA, value)

    companion object {
        private const val KEY_HAS_LOADED_DATA = "has_loaded_data"
    }

    init {
        fetchBossAccount()
        setCurrentDate()
        fetchBossStoreInfo()
    }

    fun retry() {
        hasLoadedData = false
        _stateFlow.update { state ->
            state.copy(isError = false, errorMessage = "")
        }
        fetchBossStoreInfo()
    }

    private fun fetchBossAccount() {
        viewModelScope.launch(exceptionHandler) {
            bossAccountUseCase.getBossAccount().collect { resource ->
                if (resource.code.toString() == "200") {
                    resource.data?.let { accountInfo ->
                        _stateFlow.update { state ->
                            state.copy(userName = accountInfo.name)
                        }
                    }
                }
            }
        }
    }

    private fun setCurrentDate() {
        val formatter = SimpleDateFormat("yyyy년 M월 d일 E요일", Locale.KOREAN)
        val formattedDate = formatter.format(Date())
        _stateFlow.update { state ->
            state.copy(currentDate = formattedDate)
        }
    }

    private fun fetchBossStoreInfo() {
        viewModelScope.launch(exceptionHandler) {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect { resource ->
                if (resource.code.toString() == "200") {
                    resource.data?.let { storeInfo ->
                        val storeId = storeInfo.bossStoreId ?: ""
                        _stateFlow.update { state ->
                            state.copy(bossStoreId = storeId)
                        }
                        if (storeId.isNotEmpty() && !hasLoadedData) {
                            fetchRecommendation(storeId)
                        }
                    }
                }
            }
        }
    }

    private fun fetchRecommendation(storeId: String) {
        _stateFlow.update { it.copy(isLoading = true, isError = false, errorMessage = "") }

        viewModelScope.launch(exceptionHandler) {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormatter.format(Date())

            storeRecommendationUseCase.getStoreRecommendation(storeId, today).collect { resource ->
                _stateFlow.update { state ->
                    state.copy(isLoading = false)
                }

                if (resource.code.toString() == "200") {
                    resource.data?.let { recommendation ->
                        hasLoadedData = true
                        _stateFlow.update { state ->
                            state.copy(
                                recommendationText = recommendation.text,
                                isError = false,
                                errorMessage = ""
                            )
                        }
                    }
                } else {
                    hasLoadedData = true
                    val errorMsg = resource.errorMessage?.takeIf { it.isNotBlank() }
                        ?: "추천 정보를 불러오는데 실패했습니다.\n(오류 코드: ${resource.code})"

                    _stateFlow.update { state ->
                        state.copy(
                            isError = true,
                            errorMessage = errorMsg
                        )
                    }
                }
            }
        }
    }
}
