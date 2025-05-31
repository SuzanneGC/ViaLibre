package com.ensim.vialibre

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ensim.vialibre.ui.components.DraggableBottomSheet
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.components.Menu
import com.ensim.vialibre.ui.components.UserMapView
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.ensim.vialibre.data.repository.LieuRepositoryImpl
import kotlin.math.log


class AffichageCarte : ComponentActivity() {
    companion object {
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

        val apiKey = applicationContext
            .packageManager
            .getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            .metaData
            .getString("com.google.android.geo.API_KEY")

        Log.d(TAG, "clé : $apiKey")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (apiKey != null) {
            Places.initialize(applicationContext,apiKey)
        }
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
                var isMenuOpen by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxSize()) {

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            val logoPainter = painterResource(id = R.drawable.logovl)
                            HeaderBar(
                                logo = logoPainter,
                                onMenuClick = {
                                    isMenuOpen = true
                                }
                            )
                        }
                    ) { innerPadding ->
                        DraggableBottomSheet(
                            onSearchSubmit = { query ->
                                val placesClient = Places.createClient(context)
                                val repository = LieuRepositoryImpl(placesClient)
                                repository.searchLieuByName(query)
                            },
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            sheetContent = {},
                            content = {
                                UserMapView(
                                    userLocation = userLocation.value,
                                    hasLocationPermission = ContextCompat.checkSelfPermission(
                                        this@AffichageCarte,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    ) == PackageManager.PERMISSION_GRANTED,
                                    permissionDenied = permissionDenied.value
                                )
                            }
                        )
                    }
                }
                if (isMenuOpen) {
                    Menu(
                        isMenuOpen = true,
                        onCloseMenu = { isMenuOpen = false },
                    )
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