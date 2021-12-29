package com.example.hoichoihome.data.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


const val APP_SERVER_URL = "https://prod-api.viewlift.com/content/"

class APIClient {


        companion object {
            private var apiClient: Retrofit? = null
        }

        fun getClient(url: String): Retrofit {

            if (apiClient == null) {

                apiClient = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }

            return apiClient!!
        }
}