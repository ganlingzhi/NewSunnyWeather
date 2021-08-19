package com.example.newsunnyweather.logic

import androidx.lifecycle.liveData
import com.example.newsunnyweather.logic.dao.PlaceDao
import com.example.newsunnyweather.logic.model.DailyResponse
import com.example.newsunnyweather.logic.model.Place
import com.example.newsunnyweather.logic.model.RealtimeResponse
import com.example.newsunnyweather.logic.model.Weather
import com.example.newsunnyweather.logic.network.SunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetWork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }


    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async { SunnyWeatherNetWork.getRealtimeWeather(lng, lat) }
            val realtimeResponse = deferredRealtime.await()
            val deferredDaily = async { SunnyWeatherNetWork.getDailyWeather(lng, lat) }
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException("realtime response status is ${realtimeResponse.status}" +
                            "daily response status is ${dailyResponse.status}")
                )
            }
        }
    }

    private fun <T> fire(coroutineContext: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(coroutineContext) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}