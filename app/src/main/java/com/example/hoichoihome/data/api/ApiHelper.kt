package com.example.hoichoihome.data.api

import com.example.hoichoihome.data.model.HomePageResponse

interface ApiHelper {
    suspend fun getData() : HomePageResponse
}