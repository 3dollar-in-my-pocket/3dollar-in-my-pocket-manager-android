package app.threedollars.manager.main.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
    private val bossStoreOpenUseCase: BossStoreOpenUseCase
) : BaseViewModel() {

    private val _bossStoreRetrieveMe = MutableStateFlow(BossStoreRetrieveDto().dtoToVo())
    val bossStoreRetrieveMe: StateFlow<BossStoreRetrieveVo> get() = _bossStoreRetrieveMe

    init {
        getBossStoreRetrieveMe()
    }

    private fun getBossStoreRetrieveMe() {
        viewModelScope.launch(exceptionHandler) {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect {
                if (it.code.toString() == "200") {
                    it.data?.let { data ->
                        _bossStoreRetrieveMe.emit(data.dtoToVo())
                    }
                }
            }
        }
    }

    fun storeOpen(id: String, latLng: LatLng) {
        viewModelScope.launch(exceptionHandler) {
            bossStoreOpenUseCase.postBossStoreOpen(id, latLng.latitude, latLng.longitude).collect {
                _bossStoreRetrieveMe.emit(bossStoreRetrieveMe.value.copy(openStatus = bossStoreRetrieveMe.value.openStatus.copy(status = "OPEN", openStartDateTime = getCurrentTime())))
            }
        }
    }

    fun storeClosed(id: String) {
        viewModelScope.launch(exceptionHandler) {
            bossStoreOpenUseCase.deleteBossStoreOpen(id).collect {
                _bossStoreRetrieveMe.emit(bossStoreRetrieveMe.value.copy(openStatus = bossStoreRetrieveMe.value.openStatus.copy(status = "CLOSED")))
            }
        }
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        formatter.format(Date())
        return formatter.format(Date())
    }

}