package com.example.newsunnyweather.logic.network

import com.example.newsunnyweather.SunnyWeatherApplication
import com.example.newsunnyweather.logic.model.DailyResponse
import com.example.newsunnyweather.logic.model.RealtimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String,
    ): retrofit2.Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String,
    ): retrofit2.Call<DailyResponse>
}