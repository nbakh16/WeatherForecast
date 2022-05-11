package com.nbakh.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.nbakh.weatherforecast.viewmodels.LocationViewModel

class WeatherFragment : Fragment() {
    private val TAG = "WeatherFragment"
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationViewModel.locationLiveData.observe(viewLifecycleOwner) {
            //Toast.makeText(requireActivity(), "${it.latitude}, ${it.longitude}", Toast.LENGTH_LONG).show()
            locationViewModel.fetchData()
        }
        locationViewModel.currentModelLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "${it.main.temp}")
        }
        locationViewModel.forecastModelLiveData.observe(viewLifecycleOwner) {
           
        }

        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

}