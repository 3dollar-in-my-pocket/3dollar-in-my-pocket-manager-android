package app.threedollars.manager.viewModels

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.threedollars.common.BaseViewModel
import app.threedollars.data.source.RemoteDataSource
import app.threedollars.data.store.request.StoresAroundRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    BaseViewModel() {

    fun getStoresAround(
        authorization: String,
        storesAroundRequest: StoresAroundRequest
    ) {
        viewModelScope.launch {
            remoteDataSource.getStoresAround(authorization, storesAroundRequest).collect {
                Log.e("asdasd", it.body().toString())
            }
        }
    }
}