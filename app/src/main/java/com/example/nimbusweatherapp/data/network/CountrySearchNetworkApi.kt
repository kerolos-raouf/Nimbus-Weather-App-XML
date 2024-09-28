package com.example.nimbusweatherapp.data.network

import com.example.nimbusweatherapp.BuildConfig
import com.example.nimbusweatherapp.data.model.CitiesForSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CountrySearchNetworkApi {

    @GET("city")
    suspend fun getCountryListForSearch(
        @Query("name") name: String,
        @Query("limit") limit: Int = 10,
        @Query("X-Api-Key") apiKey: String = BuildConfig.COUNTRY_SEARCH_API_KEY,
    ): Response<CitiesForSearch>

}