package com.ensim.vialibre.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ensim.vialibre.data.repository.Avis
import com.ensim.vialibre.ui.screens.LastAvisScreen
import com.ensim.vialibre.ui.screens.SetAvisScreen

@Composable
fun AvisNavHost(
    navController: NavHostController,
    placeId: String,
    avis: Avis?
) {
    NavHost(navController, startDestination = "afficherLastAvis") {
        composable("afficherLastAvis") {
            LastAvisScreen(
                placeId = placeId,
                avis = avis
            )
        }
        composable("changerAvis") {
            SetAvisScreen(
                placeId = placeId
            )
        }
    }
}