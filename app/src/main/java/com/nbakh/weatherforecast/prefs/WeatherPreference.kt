package com.nbakh.weatherforecast.prefs

import android.content.Context
import android.content.SharedPreferences

class WeatherPreference(context: Context) {
    private lateinit var preference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val tempStatus = "status"
    init {
        preference = context.getSharedPreferences("weather_pref", Context.MODE_PRIVATE)
        editor = preference.edit()
    }

    fun setTempUnitStatus(isFahrenheit: Boolean) {
        editor.putBoolean(tempStatus, isFahrenheit)
        editor.commit()
    }

    fun getTempUnitStatus() : Boolean{
        return preference.getBoolean(tempStatus, false)
    }
}