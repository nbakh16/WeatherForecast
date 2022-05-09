package com.nbakh.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.nbakh.weatherapp.viewmodels.LocationViewModel

class WeatherFragment : Fragment() {
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationViewModel.locationLiveData.observe(viewLifecycleOwner)
    }

}