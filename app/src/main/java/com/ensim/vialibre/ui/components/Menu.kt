package com.ensim.vialibre.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.AffichageCarte
import com.ensim.vialibre.MainActivity
import kotlin.math.roundToInt

@Composable
fun Menu(
    isMenuOpen: Boolean,
    onCloseMenu: () -> Unit
) {val screenWidth = LocalConfiguration.current.screenWidthDp.dp
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
        ){
            val context = LocalContext.current
            Column (Modifier.padding(top = 20.dp)) {

                Titres("Menu")

                ElementMenu (text = "Accueil") {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                ElementMenu(text = "Lieux favoris") {
                    Toast.makeText(context, "Profil cliqué", Toast.LENGTH_SHORT).show()
                }
                ElementMenu(text = "Mes avis"){
                    Toast.makeText(context, "Mes avis cliqué", Toast.LENGTH_SHORT).show()
                }
                ElementMenu(text = "Paramètres") {
                    Toast.makeText(context, "Paramètres cliqué", Toast.LENGTH_SHORT).show()
                }
                ElementMenu(text = "Déconnexion") {
                    Toast.makeText(context, "Déconnexion cliqué", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}