package com.example.hoichoihome.data.api

import com.example.hoichoihome.data.model.HomePageResponse

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getData(): HomePageResponse = apiService.getData()

}