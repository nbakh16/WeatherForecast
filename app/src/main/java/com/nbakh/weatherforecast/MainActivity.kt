package com.nbakh.weatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.nbakh.weatherforecast.network.detectUserLocation
import com.nbakh.weatherforecast.viewmodels.LocationViewModel

class MainActivity : AppCompatActivity() {

    private val locationViewModel: LocationViewModel by viewModels()

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getLocation()
                //Toast.makeText(this, "fine location granted", Toast.LENGTH_SHORT).show()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLocation()
                //Toast.makeText(this, "course location granted", Toast.LENGTH_SHORT).show()
            } else -> {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
        }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        detectUserLocation(this) {
            locationViewModel.setNewLocation(it)
        }
    }
}

fun isLocationPermissionGranted(context: Context) : Boolean {
    return ContextCompat
        .checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat
                .checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}

fun requestUserForLocationPermission(activity: Activity) {
    activity.requestPermissions(
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),
        111
    )
}

