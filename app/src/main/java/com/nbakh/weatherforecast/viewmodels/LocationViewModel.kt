package com.nbakh.weatherforecast.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbakh.weatherforecast.models.CurrentModel
import com.nbakh.weatherforecast.models.ForecastModel
import com.nbakh.weatherforecast.repos.WeatherRepository
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel(){
    val repository = WeatherRepository()
    val locationLiveData: MutableLiveData<Location> = MutableLiveData()
    val currentModelLD: MutableLiveData<CurrentModel> = MutableLiveData()
    val forecastModelLD: MutableLiveData<ForecastModel> = MutableLiveData()

    fun setNewLocation(location: Location) {
        locationLiveData.value = location
    }

    fun fetchData(status: Boolean = false) {
        viewModelScope.launch {
            try {
                currentModelLD.value = repository.fetchCurrentWeatherData(locationLiveData.value!!, status = status)
                forecastModelLD.value = repository.fetchForecastWeatherData(locationLiveData.value!!, status = status)
            }catch (e: Exception) {
                Log.e("LocationViewModel", e.localizedMessage)
            }
        }
    }
}

