package app.threedollars.manager.feature.home.preference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.threedollars.common.BaseViewModel
import app.threedollars.common.Resource
import app.threedollars.common.toDisplayErrorMessage
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.GetStorePreferenceUseCase
import app.threedollars.domain.usecase.PatchStorePreferenceUseCase
import app.threedollars.manager.feature.home.navigation.StorePreferenceRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
internal class StorePreferenceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val getStorePreferenceUseCase: GetStorePreferenceUseCase,
    private val patchStorePreferenceUseCase: PatchStorePreferenceUseCase,
) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow(
        StorePreferenceState(storeId = savedStateHandle.toRoute<StorePreferenceRoute>().storeId)
    )
    val stateFlow: StateFlow<StorePreferenceState> = _stateFlow.asStateFlow()

    private val patchMutex = Mutex()

    init {
        loadPreference()
    }

    private fun loadPreference() {
        viewModelScope.launch(exceptionHandler) {
            val storeId = _stateFlow.value.storeId.ifEmpty { fetchStoreId() }
            if (storeId.isEmpty()) {
                _stateFlow.update { it.withError(null) }
                return@launch
            }
            _stateFlow.update { it.copy(storeId = storeId) }

            val result = getStorePreferenceUseCase(storeId = storeId)
            val preference = result.data
            if (result is Resource.Success && preference != null) {
                _stateFlow.update { it.withPreference(preference) }
            } else {
                _stateFlow.update { it.withError(result.errorMessage.toDisplayErrorMessage()) }
            }
        }
    }

    private suspend fun fetchStoreId(): String =
        bossStoreRetrieveUseCase.getBossStoreRetrieveMe().firstOrNull()?.data?.bossStoreId.orEmpty()

    fun toggleRetainLocationOnClose() {
        updatePreference { it.toggleRetainLocationOnClose() }
    }

    fun toggleAutoOpenCloseControl() {
        updatePreference { it.toggleAutoOpenCloseControl() }
    }

    fun dismissError() {
        _stateFlow.update { it.dismissError() }
    }

    private fun updatePreference(transform: (StorePreferenceState) -> StorePreferenceState) {
        val before = _stateFlow.value
        val after = transform(before)
        if (after == before) return
        _stateFlow.value = after

        viewModelScope.launch(exceptionHandler) {
            patchMutex.withLock {
                val latest = _stateFlow.value
                val result = patchStorePreferenceUseCase(storeId = latest.storeId, preference = latest.toPreference())
                if (result is Resource.Error) {
                    _stateFlow.update { it.withError(result.errorMessage.toDisplayErrorMessage()) }
                }
            }
        }
    }
}
