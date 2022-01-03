package com.example.hoichoihome.repo

import com.example.hoichoihome.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper)  {

    suspend fun getData() = apiHelper.getData()

}