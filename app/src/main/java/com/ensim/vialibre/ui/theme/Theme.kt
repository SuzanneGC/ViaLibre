package com.ensim.vialibre.ui.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = VioletSombre,

    primary = ClairViolet,
    onPrimary = ClairViolet,

    secondary = ClairBleu,
    secondaryContainer = BleuSombre,
    onSecondary = BleuSombre,

    tertiary = ClairViolet,
    onTertiary = ClairViolet,

    surfaceBright = BleuSombre,
    surfaceDim = ClairBleu,
    surfaceVariant = VioletHeader
)

private val LightColorScheme = lightColorScheme(
    background = VertClair,

    primary = VioletFonce,
    onPrimary = VioletFonce,

    secondary = VioletClair,
    secondaryContainer = VioletFonce,
    onSecondary = VioletClair,

    tertiary = VioletMix,
    onTertiary = VioletMix,

    surfaceBright = VertSature,
    surfaceDim = VioletClair,
    surfaceVariant = VioletMenu
)

@Composable
fun ViaLibreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

