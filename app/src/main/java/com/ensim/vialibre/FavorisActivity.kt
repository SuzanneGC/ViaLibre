package com.ensim.vialibre

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.data.model.SettingsViewModel
import com.ensim.vialibre.data.model.ThemeViewModel
import com.ensim.vialibre.data.repository.LieuRepositoryImpl
import com.ensim.vialibre.data.repository.SettingsRepository
import com.ensim.vialibre.data.repository.getFavorisByUser
import com.ensim.vialibre.domain.Lieu
import com.ensim.vialibre.ui.components.molecules.CustomCardList
import com.ensim.vialibre.ui.components.navigation.HeaderBar
import com.ensim.vialibre.ui.components.molecules.Menu
import com.ensim.vialibre.ui.components.atoms.Titres
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavorisActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                var favoris by remember { mutableStateOf<List<String>?>(null) }
                val context = LocalContext.current

                val lieuxState = remember { mutableStateOf<List<Lieu>>(emptyList()) }

                val userId = FirebaseAuth.getInstance().currentUser?.uid

                val placesClient = Places.createClient(context)

                val lieuRepository = LieuRepositoryImpl(
                    placesClient = placesClient
                )

                val isLoading = remember { mutableStateOf(true) }

                LaunchedEffect(userId) {
                    try {
                        getFavorisByUser(userId ?: "") { favorisList ->
                            favoris = favorisList

                            CoroutineScope(Dispatchers.IO).launch {
                                val lieux = favorisList.mapNotNull { favori ->
                                    try {
                                        lieuRepository.searchLieuById(favori)
                                    } catch (e: Exception) {
                                        Log.e("LieuLoad", "Erreur récupération lieu pour $favori", e)
                                        null
                                    }
                                }

                                withContext(Dispatchers.Main) {
                                    lieuxState.value = lieux
                                    isLoading.value = false
                                }
                            }
                        }
                    } catch (_: Exception) {
                        lieuxState.value = emptyList()
                    } finally {
                        isLoading.value = false
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        val logoPainter = painterResource(id = R.drawable.logovl)
                        HeaderBar(logo = logoPainter, onMenuClick = {
                            isMenuOpen = true
                        })
                    }) { innerPadding ->

                        Column(modifier = Modifier.padding(innerPadding)) {
                            Titres("Mes favoris")
                            if (isLoading.value) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else if (lieuxState.value.isEmpty()) {
                                Text(
                                    "Vous n'avez pas encore de lieu favori.",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                CustomCardList(
                                    items = lieuxState.value,
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            }

                        }
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