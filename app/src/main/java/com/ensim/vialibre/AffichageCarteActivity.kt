package com.ensim.vialibre

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.data.model.SettingsViewModel
import com.ensim.vialibre.data.model.ThemeViewModel
import com.ensim.vialibre.data.repository.LieuRepositoryImpl
import com.ensim.vialibre.data.repository.SettingsRepository
import com.ensim.vialibre.ui.components.molecules.DraggableBottomSheet
import com.ensim.vialibre.ui.components.navigation.HeaderBar
import com.ensim.vialibre.ui.components.molecules.Menu
import com.ensim.vialibre.ui.components.molecules.UserMapView
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AffichageCarteActivity : ComponentActivity() {
    companion object {
        const val TAG = "AffichageCarteActivity"
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
        enableEdgeToEdge()

        val apiKey = applicationContext.packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("com.google.android.geo.API_KEY")

        Log.d(TAG, "clé : $apiKey")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }
        Log.d(TAG, "userLocation.value ${userLocation.value}")
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val navController = rememberNavController()

            val context = LocalContext.current

            val viewModel: ThemeViewModel = viewModel()
            val appTheme by viewModel.theme.collectAsState()


            val settingsViewModel: SettingsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val repository = SettingsRepository(applicationContext)
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(repository) as T
                }
            })
            val fontSizeScale by settingsViewModel.fontSizeScale.collectAsState()

            ViaLibreTheme(dynamicColor = false, fontSizeScale = fontSizeScale, appTheme = appTheme) {
                var isMenuOpen by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxSize()) {

                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        val logoPainter = painterResource(id = R.drawable.logovl)
                        HeaderBar(logo = logoPainter, onMenuClick = {
                            isMenuOpen = true
                        })
                    }) { innerPadding ->
                        DraggableBottomSheet(
                            lat = userLocation.value?.latitude?: 48.8566,
                            lng = userLocation.value?.longitude?: 2.3522,

                            onSearchSubmit = { query, lat, lng ->
                                val placesClient = Places.createClient(context)
                                val repository = LieuRepositoryImpl(placesClient)
                                repository.searchLieuByName(query, lat, lng)
                            },
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            sheetContent = {},
                            content = {
                                UserMapView(
                                    userLocation = userLocation.value,
                                    hasLocationPermission = ContextCompat.checkSelfPermission(
                                        this@AffichageCarteActivity,
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
                        authViewModel = authViewModel,
                        navController = navController

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