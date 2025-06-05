package com.ensim.vialibre.ui.components.molecules

import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensim.vialibre.FavorisActivity
import com.ensim.vialibre.MainActivity
import com.ensim.vialibre.MesAvisActivity
import com.ensim.vialibre.SettingsActivity
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.ui.components.atoms.ElementMenu
import com.ensim.vialibre.ui.components.atoms.Titres
import kotlin.math.roundToInt

@Composable
fun Menu(
    isMenuOpen: Boolean,
    onCloseMenu: () -> Unit,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidth.toPx() }
    val menuWidth = screenWidth / 2
    val menuWidthPx = with(LocalDensity.current) { menuWidth.toPx() }

    val offsetX = remember { Animatable(screenWidthPx) }

    LaunchedEffect(isMenuOpen) {
        offsetX.animateTo(
            targetValue = if (isMenuOpen) screenWidthPx - menuWidthPx else screenWidthPx,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onCloseMenu() }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(menuWidth)
                .offset {
                    IntOffset(
                        x = offsetX.value.roundToInt(),
                        y = 0
                    )
                }
                .background(MaterialTheme.colorScheme.background)
        ) {
            val context = LocalContext.current
            Column(Modifier.padding(top = 20.dp)) {

                Titres("Menu")

                ElementMenu(text = "Accueil") {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                ElementMenu(text = "Lieux favoris") {
                    val intent = Intent(context, FavorisActivity::class.java)
                    context.startActivity(intent)
                }
                ElementMenu(text = "Mes avis") {
                    val intent = Intent(context, MesAvisActivity::class.java)
                    context.startActivity(intent)
                }
                ElementMenu(text = "Paramètres") {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                }
                ElementMenu(text = "Déconnexion") {
                    authViewModel.logout()
                    onCloseMenu()
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}