package com.example.newsunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.newsunnyweather.logic.Repository
import com.example.newsunnyweather.logic.model.Location

class WeatherViewModel : ViewModel() {
    private val location = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    val weatherLiveData = Transformations.switchMap(location) {
        location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        location.value = Location(lng, lat)
    }
}