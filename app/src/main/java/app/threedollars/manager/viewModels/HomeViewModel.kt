package app.threedollars.manager.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.data.source.RemoteDataSource
import app.threedollars.data.store.request.StoresAroundRequest
import app.threedollars.data.store.response.StoresAroundResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    BaseViewModel() {

    private val _storesAroundResponseList = MutableLiveData<List<StoresAroundResponse>>()
    val storesAroundResponseList: LiveData<List<StoresAroundResponse>> get() = _storesAroundResponseList

    fun getStoresAround(
        authorization: String,
        storesAroundRequest: StoresAroundRequest
    ) {
        viewModelScope.launch {
            remoteDataSource.getStoresAround(authorization, storesAroundRequest).collect {
                if (it.isSuccessful) {
                    it.body()?.data?.let { list ->
                        _storesAroundResponseList.value = list
                    }
                }
            }
        }
    }
}