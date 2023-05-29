package app.threedollars.manager.storeManagement.viewModel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.BossStoreUseCase
import app.threedollars.manager.storeManagement.ui.businessschedule.ScheduleDay
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.BossStoreRetrieveVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessScheduleEditViewModel @Inject constructor(
    private val bossStoreUseCase: BossStoreUseCase,
    private val bossStoreRetrieveUseCase: BossStoreRetrieveUseCase,
) : BaseViewModel() {

    init {
        getBossStoreRetrieveMe()
    }

    private val _bossStoreRetrieveMe = MutableEventFlow<BossStoreRetrieveVo>()
    val bossStoreRetrieveMe: EventFlow<BossStoreRetrieveVo> get() = _bossStoreRetrieveMe
    private val _scheduleDays = MutableStateFlow(defaultScheduleDays)
    val scheduleDays: StateFlow<List<ScheduleDay>> = _scheduleDays

    private fun getBossStoreRetrieveMe() {
        viewModelScope.launch(exceptionHandler) {
            bossStoreRetrieveUseCase.getBossStoreRetrieveMe().collect {
                if (it.code.toString() == "200") {
                    it.data.dtoToVo().let { data ->
                        _bossStoreRetrieveMe.emit(data)
                        val scheduleDays = defaultScheduleDays.toMutableList()
                        data.appearanceDays.forEach { appearance ->
                            val index = scheduleDays.indexOfFirst { it.dayOfTheWeek == appearance.dayOfTheWeek }
                            scheduleDays[index] = scheduleDays[index].copy(
                                startTime = appearance.openingHours.startTime,
                                endTime = appearance.openingHours.endTime,
                                locationDescription = appearance.locationDescription,
                                isSelected = true
                            )
                        }
                        _scheduleDays.emit(scheduleDays)
                    }
                }
            }
        }
    }

    fun selectedScheduleDays(scheduleDay: ScheduleDay) {
        viewModelScope.launch(exceptionHandler) {
            val index = scheduleDays.value.indexOfFirst { it.dayOfTheWeek == scheduleDay.dayOfTheWeek }
            val list = scheduleDays.value.toMutableList()
            list[index] = scheduleDay
            _scheduleDays.emit(list)
        }
    }
}

val defaultScheduleDays = listOf(
    ScheduleDay("월", "MONDAY", "", "", "", false),
    ScheduleDay("화", "TUESDAY", "", "", "", false),
    ScheduleDay("수", "WEDNESDAY", "", "", "", false),
    ScheduleDay("목", "THURSDAY", "", "", "", false),
    ScheduleDay("금", "FRIDAY", "", "", "", false),
    ScheduleDay("토", "SATURDAY", "", "", "", false),
    ScheduleDay("일", "SUNDAY", "", "", "", false),
)