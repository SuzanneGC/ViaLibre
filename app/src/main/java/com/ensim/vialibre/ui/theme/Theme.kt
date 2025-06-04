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
import com.ensim.vialibre.ui.accessibility.AppTheme
import com.ensim.vialibre.ui.accessibility.FontSizeScale
import com.ensim.vialibre.ui.accessibility.customTypography

private val DarkColorScheme = darkColorScheme(
    background = VioletSombre,

    primary = ClairViolet,
    onPrimary = VioletSombre,

    secondary = ClairBleu,
    secondaryContainer = BleuSombre,
    onSecondary = BleuSombre,

    tertiary = ClairViolet,
    onTertiary = ClairViolet,

    surfaceContainer = BleuSombre,

    surfaceBright = BleuSombre,
    surfaceDim = ClairBleu,
    surfaceVariant = VioletHeader
)

private val LightColorScheme = lightColorScheme(
    background = VertClair,

    primary = VioletFonce,
    onPrimary = VioletClair,

    secondary = VioletClair,
    secondaryContainer = VioletFonce,
    onSecondary = VioletClair,

    tertiary = VioletMix,
    onTertiary = VioletMix,

    surfaceContainer = VioletClair,

    surfaceBright = VertSature,
    surfaceDim = VertClair,
    surfaceVariant = VioletMenu
)

@Composable
fun ViaLibreTheme(
    appTheme: AppTheme,
    //darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    fontSizeScale: FontSizeScale,
    content: @Composable () -> Unit
) {

    val isDarkTheme = when (appTheme){
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }


    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = customTypography(fontSizeScale),
        content = content
    )
}

