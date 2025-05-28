package com.ensim.vialibre

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ensim.vialibre.ui.components.DraggableBottomSheet
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.components.UserMapView
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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

    @OptIn(ExperimentalMaterial3Api::class)
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
            /*val context = LocalContext.current
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
            }*/
            val context = LocalContext.current
            ViaLibreTheme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val logoPainter = painterResource(id = R.drawable.logovl)
                        HeaderBar(
                            logo = logoPainter,
                            /*onMenuClick = {
                                // Action menu (ici un Toast pour l'exemple)
                                Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show()
                            }*/
                        )
                    }
                ) { innerPadding ->
                    DraggableBottomSheet(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        sheetContent = {
                            // Contenu de la feuille draggable, par exemple :
                            Text(
                                "Informations complémentaires",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                            // Tu peux mettre plus de contenu ici...
                        },
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