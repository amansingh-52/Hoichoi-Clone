package com.example.hoichoihome.repo

import com.example.hoichoihome.data.api.APIClient
import com.example.hoichoihome.data.api.APP_SERVER_URL
import com.example.hoichoihome.data.api.ApiInterface

class HomeRepo {
    private val api =
        APIClient().getClient(APP_SERVER_URL).create(ApiInterface::class.java)

    suspend fun getHomeData() = api.getData()

}