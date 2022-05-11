package com.nbakh.weatherforecast.repos

import android.location.Location
import com.nbakh.weatherforecast.models.CurrentModel
import com.nbakh.weatherforecast.models.ForecastModel
import com.nbakh.weatherforecast.network.NetworkService
import com.nbakh.weatherforecast.network.weather_api_key

class WeatherRepository {
    suspend fun fetchCurrentWeatherData(location: Location) : CurrentModel{
        val end_url = "weather?lat=${location.latitude}&lon=${location.longitude}&units=metric&appid=${weather_api_key}"
        return NetworkService.weatherServiceApi.getCurrentWeatherData(end_url)
    }

    suspend fun fetchForecastWeatherData(location: Location) : ForecastModel{
        val end_url = "forecast?lat=${location.latitude}&lon=${location.longitude}&units=metric&appid=${weather_api_key}"
        return NetworkService.weatherServiceApi.getForecastWeatherData(end_url)
    }
}