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

class LocationViewModel : ViewModel() {
    val repository = WeatherRepository()
    val locationLiveData : MutableLiveData<Location> = MutableLiveData()
    val currentModelLiveData : MutableLiveData<CurrentModel> = MutableLiveData()
    val forecastModelLiveData : MutableLiveData<ForecastModel> = MutableLiveData()

    fun setNewLocation(location: Location){
        locationLiveData.value = location
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                currentModelLiveData.value = repository.fetchCurrentWeatherData(locationLiveData.value!!)
                forecastModelLiveData.value = repository.fetchForecastWeatherData(locationLiveData.value!!)
            } catch (e: Exception) {
                Log.d("LocationViewModel", e.localizedMessage)
            }
        }
    }
}

