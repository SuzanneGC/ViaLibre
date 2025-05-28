package com.ensim.vialibre

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ensim.vialibre.ui.components.UserMapView
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AffichageCarte : ComponentActivity() {
    companion object{
        const val TAG = "AffichageCarte"
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLocation: State<LatLng?> = _userLocation

    private var _permissionDenied = mutableStateOf(false)
    val permissionDenied: State<Boolean> = _permissionDenied

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLastLocation()
        } else {
            _permissionDenied.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Log.d(TAG, "userLocation.value ${userLocation.value}")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        setContent {
            val context = LocalContext.current
            ViaLibreTheme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        UserMapView(
                            userLocation = userLocation.value,
                            hasLocationPermission = ContextCompat.checkSelfPermission(
                                this@AffichageCarte,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED,
                            permissionDenied = permissionDenied.value
                        )
                    }
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        lifecycleScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    _userLocation.value = LatLng(it.latitude, it.longitude)
                }
            } catch (e: Exception) {

            }
        }
    }
}