package com.ensim.vialibre

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.data.model.SettingsViewModel
import com.ensim.vialibre.data.model.ThemeViewModel
import com.ensim.vialibre.data.repository.Avis
import com.ensim.vialibre.data.repository.SettingsRepository
import com.ensim.vialibre.data.repository.getLastAvisById
import com.ensim.vialibre.data.utils.getLatLngFromPlaceId
import com.ensim.vialibre.ui.components.AvisNavHost
import com.ensim.vialibre.ui.components.CustomCard
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.components.MapCenteredOnPlace
import com.ensim.vialibre.ui.components.Menu
import com.ensim.vialibre.ui.components.Titres
import com.ensim.vialibre.ui.screens.PresentationScreen
import com.ensim.vialibre.ui.screens.ScreenCarousel
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PresentationLieu : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: "nom inconnu"
        val address = intent.getStringExtra("address") ?: "adresse inconnue"
        val photo = intent.getStringExtra("photoRef")

        val placeId = intent.getStringExtra("placeId")

        val TAG = "PresentationLieu"

        Log.d(TAG, "placeID initialisé : $placeId")


        enableEdgeToEdge()
        setContent {

            val settingsViewModel: SettingsViewModel =
                viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val repository = SettingsRepository(applicationContext)
                        @Suppress("UNCHECKED_CAST") return SettingsViewModel(repository) as T
                    }
                })
            val fontSizeScale by settingsViewModel.fontSizeScale.collectAsState()

            val viewModel: ThemeViewModel = viewModel()
            val appTheme by viewModel.theme.collectAsState()





            ViaLibreTheme(
                dynamicColor = false, fontSizeScale = fontSizeScale, appTheme = appTheme
            ) {
                var isMenuOpen by remember { mutableStateOf(false) }

                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                var avis by remember { mutableStateOf<List<Avis>>(emptyList()) }
                var lastAvis by remember { mutableStateOf<Avis?>(null) }

                val context = LocalContext.current

                val latLngState = remember { mutableStateOf<LatLng?>(null) }
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(placeId) {
                    Log.d(TAG, "Lancement")
                    try {
                        lastAvis = getLastAvisById(placeId ?: "")
                        Log.d("LastAvis", "Avis reçu : $lastAvis")
                        coroutineScope.launch(Dispatchers.IO) {
                            val latLng = placeId?.let { getLatLngFromPlaceId(context, it) }
                            latLngState.value = latLng
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Erreur récupération avis", e)
                        avis = emptyList()
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        val logoPainter = painterResource(id = R.drawable.logovl)
                        HeaderBar(logo = logoPainter, onMenuClick = {
                            isMenuOpen = true
                        })
                    }) { innerPadding ->
                        ScreenCarousel(
                            modifier = Modifier
                                .padding(innerPadding)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            name = name,
                            address = address,
                            photo = photo,
                            placeId = placeId?:"",
                            latLngState = latLngState,
                            avis = lastAvis
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
}