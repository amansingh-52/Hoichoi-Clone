package com.example.hoichoihome.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoichoihome.data.model.HomePageResponse
import com.example.hoichoihome.repo.HomeRepo
import com.example.hoichoihome.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val homeRepo = HomeRepo()

    val getHomeData = MutableLiveData<Resource<HomePageResponse>>()


    private val homeDataExceptionHandler = CoroutineExceptionHandler{ _ , throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            Log.e("HomeDataExp: ", throwable.message.toString())
            getHomeData.value = Resource.error(message = throwable.message.toString(), data = null)
        }
    }

    fun getDataForHome(){
        getHomeData.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO + homeDataExceptionHandler) {
            homeRepo.getHomeData().let {
                Log.d("HomeData: ", it.toString())
                withContext(Dispatchers.Main){
                    getHomeData.value = Resource.success(data = it)
                }
            }
        }
    }



}