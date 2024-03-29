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
    private val _enableComplete = MutableEventFlow<Boolean>()
    val enableComplete: EventFlow<Boolean> get() = _enableComplete
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
                                isSelected = true,
                            )
                        }
                        _scheduleDays.emit(scheduleDays)
                        scheduleDays.forEach { scheduleDays ->
                            if (scheduleDays.startTime.isNotEmpty() && scheduleDays.endTime.isNotEmpty()) {
                                appearanceDays[scheduleDays.dayOfTheWeek] = AppearanceDaysVo(
                                    dayOfTheWeek = scheduleDays.dayOfTheWeek,
                                    openingHours = OpeningHoursVo(startTime = scheduleDays.startTime, endTime = scheduleDays.endTime),
                                    locationDescription = scheduleDays.locationDescription,
                                )
                            }
                        }
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

                        it.locationDescription,
                    )
                },
            ).collect {
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
                if (it.isSelected) {
                    appearanceDays[it.dayOfTheWeek] = AppearanceDaysVo(
                        it.dayOfTheWeek,
                        OpeningHoursVo(it.startTime, it.endTime),
                        it.locationDescription,
                    )
                } else {
                    appearanceDays.remove(it.dayOfTheWeek)
                }
            }
            isEditEnable()
        }
    }

    fun editDaysStartTime(day: String, time: String) {
        val appearanceDaysVo = appearanceDays[day] ?: return
        val openingHours = appearanceDaysVo.openingHours.copy(startTime = time)
        appearanceDays[day] = appearanceDaysVo.copy(openingHours = openingHours)
        isEditEnable()
    }

    fun editDaysEndTime(day: String, time: String) {
        val appearanceDaysVo = appearanceDays[day] ?: return
        val openingHours = appearanceDaysVo.openingHours.copy(endTime = time)
        appearanceDays[day] = appearanceDaysVo.copy(openingHours = openingHours)
        isEditEnable()
    }

    fun editDaysLocationDescription(day: String, locationDescription: String) {
        val appearanceDaysVo = appearanceDays[day] ?: return
        appearanceDays[day] = appearanceDaysVo.copy(locationDescription = locationDescription)
        isEditEnable()
    }

    private fun isEditEnable() {
        viewModelScope.launch(exceptionHandler) {
            appearanceDays.values.forEach {
                if (it.openingHours.startTime.isEmpty() || it.openingHours.endTime.isEmpty()) {
                    _enableComplete.emit(false)
                    return@launch
                }
            }
            _enableComplete.emit(true)
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
