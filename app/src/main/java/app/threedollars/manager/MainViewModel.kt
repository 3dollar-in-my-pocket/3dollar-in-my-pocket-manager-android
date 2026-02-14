package app.threedollars.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.threedollars.domain.usecase.BossAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bossAccountUseCase: BossAccountUseCase
) : ViewModel() {

    private val _enableSalesAIRecommendation = MutableStateFlow(false)
    val enableSalesAIRecommendation: StateFlow<Boolean> = _enableSalesAIRecommendation.asStateFlow()

    init {
        getBossAccount()
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
