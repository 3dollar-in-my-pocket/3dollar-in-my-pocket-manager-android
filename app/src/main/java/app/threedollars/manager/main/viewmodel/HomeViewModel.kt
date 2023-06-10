package app.threedollars.manager.main.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.dto.BossStoreRetrieveDto
import app.threedollars.domain.usecase.BossStoreOpenUseCase
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.BossStoreRetrieveVo
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val bossStoreOpenUseCase: BossStoreOpenUseCase
) : BaseViewModel() {

    private val _bossStoreRetrieveMe = MutableEventFlow<BossStoreRetrieveVo>()
    val bossStoreRetrieveMe: EventFlow<BossStoreRetrieveVo> get() = _bossStoreRetrieveMe
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
                        _bossStoreRetrieveMe.emit(data.dtoToVo())
                        _storeOpen.emit(data.openStatus?.status == "OPEN")
                    }
                }
            }
        }
    }

    fun storeOpen(id: String, latLng: LatLng) {
        viewModelScope.launch(exceptionHandler) {
            bossStoreOpenUseCase.postBossStoreOpen(id, latLng.latitude, latLng.longitude).collect {
                getBossStoreRetrieveMe()
            }
        }
    }

    fun storeStop(id:String){
        viewModelScope.launch(exceptionHandler) {
            bossStoreOpenUseCase.deleteBossStoreOpen(id).collect {
                getBossStoreRetrieveMe()
            }
        }
    }

}