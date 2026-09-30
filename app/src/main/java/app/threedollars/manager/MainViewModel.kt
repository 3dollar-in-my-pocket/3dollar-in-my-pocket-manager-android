package app.threedollars.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.threedollars.domain.usecase.BossAccountUseCase
import app.threedollars.domain.usecase.MessageGuideUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bossAccountUseCase: BossAccountUseCase,
    private val messageGuideUseCase: MessageGuideUseCase,
) : ViewModel() {

    private val _enableSalesAIRecommendation = MutableStateFlow(false)
    val enableSalesAIRecommendation: StateFlow<Boolean> = _enableSalesAIRecommendation.asStateFlow()

    private val _showMessageTooltip = MutableStateFlow(false)
    val showMessageTooltip: StateFlow<Boolean> = _showMessageTooltip.asStateFlow()

    init {
        getBossAccount()
        showMessageTooltipIfNeeded()
    }

    private fun showMessageTooltipIfNeeded() {
        viewModelScope.launch {
            if (messageGuideUseCase.isMainTabTooltipShown().first()) return@launch
            messageGuideUseCase.markMainTabTooltipShown()
            _showMessageTooltip.value = true
        }
    }

    fun hideMessageTooltip() {
        _showMessageTooltip.value = false
    }

    private fun getBossAccount() {
        viewModelScope.launch {
            bossAccountUseCase.getBossAccount().collect { resource ->
                if (resource.code == "200") {
                    resource.data?.let { dto ->
                        _enableSalesAIRecommendation.value = dto.settings.enableSalesAIRecommendation
                    }
                }
            }
        }
    }
}
