package app.threedollars.manager.feature.ai

import app.threedollars.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class AiViewModel @Inject constructor() : BaseViewModel() {

    private val _stateFlow: MutableStateFlow<AiState> = MutableStateFlow(AiState())

    val stateFlow: StateFlow<AiState> = _stateFlow.asStateFlow()
}
