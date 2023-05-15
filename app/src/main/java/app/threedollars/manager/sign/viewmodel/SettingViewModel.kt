package app.threedollars.manager.sign.viewmodel

import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.common.EventFlow
import app.threedollars.common.MutableEventFlow
import app.threedollars.domain.usecase.AuthUseCase
import app.threedollars.domain.usecase.BossAccountUseCase
import app.threedollars.domain.usecase.FaqUseCase
import app.threedollars.manager.util.dtoToVo
import app.threedollars.manager.vo.BossAccountInfoVo
import app.threedollars.manager.vo.FaqVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val bossAccountUseCase: BossAccountUseCase,
    private val faqUseCase: FaqUseCase
) : BaseViewModel() {

    private val _bossAccountInfo = MutableEventFlow<BossAccountInfoVo>()
    val bossAccountInfo: EventFlow<BossAccountInfoVo> get() = _bossAccountInfo

    private val _faq = MutableEventFlow<List<FaqVo>>()
    val faq: EventFlow<List<FaqVo>> get() = _faq

    private val _isSuccess = MutableEventFlow<Boolean>()
    val isSuccess: EventFlow<Boolean> get() = _isSuccess

    fun getBossAccount() {
        viewModelScope.launch {
            bossAccountUseCase.getBossAccount().collect {
                if (it.code == "200") {
                    it.data?.let { dto ->
                        _bossAccountInfo.emit(dto.dtoToVo())
                    }
                }
            }
        }
    }

    fun getFaq() {
        viewModelScope.launch {
            faqUseCase.getFaqs().collect {
                if (it.code == "200") {
                    it.data?.let { dtoList ->
                        _faq.emit(dtoList.map { dto -> dto.dtoToVo() }.sortedBy { vo -> vo.categoryInfo.displayOrder })
                    }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCase.signOut().collect {
                if (it.data == "OK") {
                    authUseCase.saveAccessToken("").collect {
                        _isSuccess.emit(true)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout().collect {
                if (it.data == "OK") {
                    authUseCase.saveAccessToken("").collect {
                        _isSuccess.emit(true)
                    }
                }
            }
        }
    }
}