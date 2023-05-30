package app.threedollars.manager.storeManagement.viewModel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.dto.AppearanceDaysRequestDto
import app.threedollars.domain.usecase.BossStoreRetrieveUseCase
import app.threedollars.domain.usecase.BossStoreUseCase
import app.threedollars.manager.storeManagement.ui.businessschedule.ScheduleDay
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.AppearanceDaysVo
import app.threedollars.manager.vo.BossStoreRetrieveVo
import app.threedollars.manager.vo.OpeningHoursVo
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
    private var appearanceDays = hashMapOf<String, AppearanceDaysVo>()
    private val _editComplete = MutableEventFlow<Boolean>()
    val editComplete: EventFlow<Boolean> get() = _editComplete

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

    fun patchBossStore(id: String) {
        viewModelScope.launch(exceptionHandler) {
            bossStoreUseCase.patchBossStore(
                bossStoreId = id,
                appearanceDays = appearanceDays.values.map {
                    AppearanceDaysRequestDto(
                        it.dayOfTheWeek,
                        it.openingHours.startTime,
                        it.openingHours.endTime,
                        it.locationDescription
                    )
                }).collect {
                if (it.code == "200") _editComplete.emit(true)
            }
        }
    }

    fun selectedScheduleDays(scheduleDay: ScheduleDay) {
        viewModelScope.launch(exceptionHandler) {
            val index = scheduleDays.value.indexOfFirst { it.dayOfTheWeek == scheduleDay.dayOfTheWeek }
            val list = scheduleDays.value.toMutableList()
            list[index] = scheduleDay
            _scheduleDays.emit(list)
            _scheduleDays.value.forEach {
                appearanceDays[it.dayOfTheWeek] = AppearanceDaysVo(
                    it.dayOfTheWeek,
                    OpeningHoursVo(it.startTime, it.endTime),
                    it.locationDescription
                )
            }
        }
    }

    fun editDaysStartTime(day: String, time: String) {
        val appearanceDaysVo = appearanceDays[day] ?: return
        val openingHours = appearanceDaysVo.openingHours.copy(startTime = time)
        appearanceDays[day] = appearanceDaysVo.copy(openingHours = openingHours)
    }

    fun editDaysEndTime(day: String, time: String) {
        val appearanceDaysVo = appearanceDays[day] ?: return
        val openingHours = appearanceDaysVo.openingHours.copy(endTime = time)
        appearanceDays[day] = appearanceDaysVo.copy(openingHours = openingHours)
    }

    fun editDaysLocationDescription(day: String, locationDescription: String) {
        val appearanceDaysVo = appearanceDays[day] ?: return
        appearanceDays[day] = appearanceDaysVo.copy(locationDescription = locationDescription)
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