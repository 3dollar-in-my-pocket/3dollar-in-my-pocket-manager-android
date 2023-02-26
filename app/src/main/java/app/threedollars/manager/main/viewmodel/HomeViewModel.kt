package app.threedollars.manager.main.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.dto.BossStoreRetrieveDto
import app.threedollars.domain.usecase.BossStoreOpenUseCase
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val bossStoreOpenUseCase: BossStoreOpenUseCase
) : BaseViewModel() {

    private val _bossStoreRetrieveMe = MutableEventFlow<BossStoreRetrieveDto>()
    val bossStoreRetrieveMe: EventFlow<BossStoreRetrieveDto> get() = _bossStoreRetrieveMe
    private val _storeOpen = MutableEventFlow<Boolean>()
    val storeOpen: EventFlow<Boolean> get() = _storeOpen

    init {
        getBossStoreRetrieveMe()
    }

    private fun getBossStoreRetrieveMe() {
        viewModelScope.launch(exceptionHandler) {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect {
                if (it.code.toString() == "200") {
                    it.data?.let { data ->
                        _bossStoreRetrieveMe.emit(data)
                        _storeOpen.emit(data.openStatus?.status == "OPEN")
                    }
                }
            }
        }
    }

    private fun storeOpen(id: String, latLng: LatLng) {
        viewModelScope.launch(exceptionHandler) {
            bossStoreOpenUseCase.postBossStoreOpen(id, latLng.latitude, latLng.longitude).collect {
                _storeOpen.emit(it.code.toString() == "200")
            }
        }
    }

}