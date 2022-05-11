package com.nbakh.weatherforecast.network

import com.nbakh.weatherforecast.models.CurrentModel
import com.nbakh.weatherforecast.models.ForecastModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

const val weather_api_key = "c196f599dc87c16a5dabf0f7ab041ad6"
const val base_url = "https://api.openweathermap.org/data/2.5/"

val retrofit = Retrofit.Builder()
    .baseUrl(base_url)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherServiceApi {
    @GET()
    fun getCurrentWeatherData(@Url endUrl: String) : CurrentModel

    @GET()
    fun getForecastWeatherData(@Url endUrl: String) : ForecastModel
}

object NetworkService {
    val weatherServiceApi: WeatherServiceApi by lazy { //Lazy >> only initializes when it's called
        retrofit.create(WeatherServiceApi::class.java)
    }
}