package com.nbakh.weatherforecast

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nbakh.weatherforecast.databinding.FragmentWeatherBinding
import com.nbakh.weatherforecast.models.CurrentModel
import com.nbakh.weatherforecast.network.getFormattedDate
import com.nbakh.weatherforecast.network.icon_url_prefix
import com.nbakh.weatherforecast.network.icon_url_suffix
import com.nbakh.weatherforecast.network.weather_api_key
import com.nbakh.weatherforecast.prefs.WeatherPreference
import com.nbakh.weatherforecast.viewmodels.LocationViewModel
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {
    private val TAG = "WeatherFragment"
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var preference: WeatherPreference
    private val locationViewModel: LocationViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preference = WeatherPreference(requireContext())
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val llm = LinearLayoutManager(requireActivity())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.forecastRV.layoutManager = llm
        locationViewModel.locationLiveData.observe(viewLifecycleOwner) {
            Log.e(TAG, "${it.latitude} ${it.longitude}")
            locationViewModel.fetchData(status = preference.getTempUnitStatus())
        }
        locationViewModel.currentModelLD.observe(viewLifecycleOwner) {
            setCurrentData(it)
        }
        locationViewModel.forecastModelLD.observe(viewLifecycleOwner) {
            Log.e(TAG, "${it.list.size}")
        }


        return binding.root
    }

    private fun setCurrentData(it: CurrentModel) {
        binding.dateTV.text = getFormattedDate(it.dt, "MMM dd, yyyy HH:mm")
        binding.locationTV.text = "${it.name}, ${it.sys.country}"
        val iconUrl = "$icon_url_prefix${it.weather[0].icon}${icon_url_suffix}"
        Glide.with(requireActivity()).load(iconUrl).into(binding.weatherIconIV)
        binding.tempTv.text = it.main.temp.roundToInt().toString()
        binding.feelsLikeTV.text = "feels like: ${it.main.feelsLike.roundToInt()}"
        binding.weatherConditionTV.text = it.weather[0].description
        binding.humidityTV.text = it.main.humidity.toString()
        binding.pressureTV.text = it.main.pressure.toString()
    }

}