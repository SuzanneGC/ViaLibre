package com.ensim.vialibre

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.data.repository.Avis
import com.ensim.vialibre.data.repository.getAvisByPlaceId
import com.ensim.vialibre.data.repository.getPlaceById
import com.ensim.vialibre.data.repository.setAvis
import com.ensim.vialibre.ui.components.CustomCard
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.components.IconeEcrireAvis
import com.ensim.vialibre.ui.components.Menu
import com.ensim.vialibre.ui.components.Titres
import com.ensim.vialibre.ui.components.ToggleAndButton
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.android.libraries.places.api.model.kotlin.place

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

            ViaLibreTheme(dynamicColor = false) {
                var isMenuOpen by remember { mutableStateOf(false) }
                val context = LocalContext.current

                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                var avis by remember { mutableStateOf<List<Avis>>(emptyList()) }

                LaunchedEffect(placeId) {
                    Log.d(TAG, "Lancement")
                    try {
                        val avisList = getAvisByPlaceId(placeId?:"")
                        avis = avisList
                        Log.d(TAG, "Avis récupérés : ${avis.size}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Erreur récupération avis", e)
                        avis = emptyList()
                    }
                }

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
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CustomCard(
                                title = name,
                                description = address,
                                image = photo,
                                placeId = placeId,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                targetActivity = MainActivity::class.java
                            )

                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Titres("Presentation du lieu")

                            ToggleAndButton { currentToggleValue ->
                                val newAvis: Avis = Avis(champ1 = currentToggleValue, placeId = placeId?:"")
                                setAvis(newAvis,{ Log.d(TAG, "Avis posté")})
                            }

                            LazyColumn(

                                modifier = Modifier.fillMaxSize()
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(avis) { item ->
                                    Log.d(TAG,"ID : " + item.placeId)
                                    Text("Avis - Champ1 : ${item.champ1}")
                                }

                            }
                            IconeEcrireAvis(
                                context = context,
                                placeName = name,
                                placeAdress = address,
                                placeImage = photo,
                                modifier = Modifier,
                                targetActivity = PresentationLieu::class.java,
                                placeId = placeId?:"",
                            )
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