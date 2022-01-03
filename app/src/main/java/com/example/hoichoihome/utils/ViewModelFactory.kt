package com.example.hoichoihome.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hoichoihome.data.api.ApiHelper
import com.example.hoichoihome.repo.MainRepository
import com.example.hoichoihome.ui.main.viewmodel.MainViewModel
import java.lang.IllegalStateException

class ViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
          return  MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalStateException("Unknown class name")
    }
}