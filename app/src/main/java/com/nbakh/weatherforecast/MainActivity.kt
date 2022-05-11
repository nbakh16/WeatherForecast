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
import com.google.android.gms.location.FusedLocationProviderClient
import com.nbakh.weatherforecast.viewmodels.LocationViewModel

class MainActivity : AppCompatActivity() {
    private val locationViewModel : LocationViewModel by viewModels()

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                //Toast.makeText(this, "Fine Location Granted", Toast.LENGTH_SHORT).show()
                detectUserLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                //Toast.makeText(this, "Coarse Location Granted", Toast.LENGTH_SHORT).show()
                detectUserLocation()
            } else -> {
            // No location access granted.
            //Toast.makeText(this, "NO Location Granted", Toast.LENGTH_SHORT).show()
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
    private fun detectUserLocation(){
        val provider = FusedLocationProviderClient(this)
        provider.lastLocation.addOnCompleteListener { 
            if(it.isSuccessful) {
                val location = it.result
                locationViewModel.setNewLocation(location)
                Toast.makeText(this, "${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

fun isLocationPermissionGranted(context: Context) : Boolean {
    return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

//Older way
fun requestUserForLocationPermission(activity: Activity) {
    activity.requestPermissions(
        arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        111
    )
}