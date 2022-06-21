package app.threedollars.manager.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.data.source.RemoteDataSource
import app.threedollars.data.store.response.MyAccountResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    BaseViewModel() {

    private val _myAccountResponse = MutableLiveData<MyAccountResponse>()
    val myAccountResponse: LiveData<MyAccountResponse> = _myAccountResponse

    init {
        getMyAccount("Bearer e9a1708e-3c2a-4dd4-a89e-58a85b5d1f75")
    }

    private fun getMyAccount(authorization: String) {
        viewModelScope.launch {
            remoteDataSource.getMyAccount(authorization).collect {
                if (it.isSuccessful) {
                    it.body()?.data?.let { response ->
                        _myAccountResponse.value = response
                    }
                }
            }
        }
    }
}