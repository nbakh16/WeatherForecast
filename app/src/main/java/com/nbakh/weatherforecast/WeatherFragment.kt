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
import com.nbakh.weatherforecast.adapters.ForecastAdapter
import com.nbakh.weatherforecast.databinding.FragmentWeatherBinding
import com.nbakh.weatherforecast.models.CurrentModel
import com.nbakh.weatherforecast.network.*
import com.nbakh.weatherforecast.prefs.WeatherPreference
import com.nbakh.weatherforecast.viewmodels.LocationViewModel
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var preference: WeatherPreference
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.item_search)
            .actionView as SearchView
        searchView.queryHint = "Enter any city name"
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    convertCityToLatLng(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_location) {
            detectUserLocation(requireContext()) {
                locationViewModel.setNewLocation(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun convertCityToLatLng(query: String) {
        val geoCoder = Geocoder(requireActivity())
        val addressList = geoCoder.getFromLocationName(query, 1)
        if (addressList.isNotEmpty()){
            val lat = addressList[0].latitude;
            val lng = addressList[0].longitude;
            val location = Location("").apply {
                latitude = lat
                longitude = lng
            }
            locationViewModel.setNewLocation(location)
        }
        else {
            Toast.makeText(requireActivity(), "Invalid City!!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preference = WeatherPreference(requireContext())
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.tempSwitch.isChecked = preference.getTempUnitStatus()
        val adapter = ForecastAdapter()
        val llm = LinearLayoutManager(requireActivity())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.forecastRV.layoutManager = llm
        binding.forecastRV.adapter = adapter
        locationViewModel.locationLiveData.observe(viewLifecycleOwner) {
            locationViewModel.fetchData(isFahrenheit = preference.getTempUnitStatus())
            locationViewModel.fetchData()
        }
        locationViewModel.currentModelLD.observe(viewLifecycleOwner) {
            setCurrentData(it)
        }
        locationViewModel.forecastModelLD.observe(viewLifecycleOwner) {
            adapter.submitList(it.list)
        }

        binding.tempSwitch.setOnCheckedChangeListener { compoundButton, isFahrenheit ->
            preference.setTempUnitStatus(isFahrenheit)
            locationViewModel.fetchData(isFahrenheit)
        }

        return binding.root
    }

    private fun setCurrentData(it: CurrentModel) {
        binding.dateTV.text = getFormattedDate(it.dt, "MMM dd, hh:mmaa")
        binding.locationTV.text = "${it.name}, ${it.sys.country}"
        val iconUrl = "$icon_url_prefix${it.weather[0].icon}${icon_url_suffix}"
        Glide.with(requireActivity()).load(iconUrl).into(binding.weatherIconIV)

        if(binding.tempSwitch.isChecked){
            binding.tempTv.text = it.main.temp.roundToInt().toString() + "\u2109"
            binding.feelsLikeTV.text = "feels like: ${it.main.feelsLike.roundToInt()}" + "\u2109"
        }
        else {
            binding.tempTv.text = it.main.temp.roundToInt().toString() + "\u2103"
            binding.feelsLikeTV.text = "feels like: ${it.main.feelsLike.roundToInt()}" + "\u2103"
        }

        binding.weatherConditionTV.text = it.weather[0].description
        binding.humidityPressureTV.text = "Humidity: ${it.main.humidity}%, Pressure: ${it.main.pressure}"

        binding.sunriseTV.text = getFormattedDate(it.sys.sunrise, "hh:mmaa")
        binding.sunsetTV.text = getFormattedDate(it.sys.sunset, "hh:mmaa")
    }

}