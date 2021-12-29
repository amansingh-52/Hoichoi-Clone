package com.example.hoichoihome.data.api

import com.example.hoichoihome.data.model.HomePageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


        @GET("pages/")
        suspend fun getData(
            @Query("site") site : String = "hoichoitv",
            @Query("pageId") pageId : String = "e097a2eb-9fcb-47aa-9c99-99684e391ba3",
            @Query("includeContent") includeContent : Boolean = true,
            @Query("languageCode") languageCode : String = "en",
            @Query("moduleOffset") moduleOffset : Int = 1,
            @Query("moduleLimit") moduleLimit : Int = 20,
            @Query("countryCode") countryCode : String = "IN"
        ): HomePageResponse



}