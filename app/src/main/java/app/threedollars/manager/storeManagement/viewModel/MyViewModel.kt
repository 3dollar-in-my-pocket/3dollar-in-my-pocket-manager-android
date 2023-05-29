package app.threedollars.manager.storeManagement.viewModel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.BossStoreUseCase
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.BossStoreRetrieveVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val bossStoreUseCase: BossStoreUseCase
) : BaseViewModel() {

    private val _bossStoreRetrieveMe = MutableEventFlow<BossStoreRetrieveVo>()
    val bossStoreRetrieveMe: EventFlow<BossStoreRetrieveVo> get() = _bossStoreRetrieveMe

    private val _isSuccess = MutableEventFlow<Boolean>()
    val isSuccess: EventFlow<Boolean> get() = _isSuccess
    fun getBossStoreRetrieveMe() {
        viewModelScope.launch {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect {
                if (it.code.toString() == "200") {
                    it.data?.let { data ->
                        _bossStoreRetrieveMe.emit(data.dtoToVo())

                    }
                }
            }
        }
    }

    fun patchIntroduction(bossStoreId: String?, introduction: String) {
        viewModelScope.launch {
            if (bossStoreId != null) {
                bossStoreUseCase.patchBossStore(bossStoreId, introduction = introduction).collect {
                    _isSuccess.emit(it.data == "OK")
                }
            }
        }
    }

}