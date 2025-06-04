package com.ensim.vialibre.ui.accessibility

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


fun customTypography(scale: FontSizeScale): Typography {
    return Typography(
        displayLarge = TextStyle(fontSize = 32.sp * scale.scaleFactor),
        headlineMedium = TextStyle(fontSize = 24.sp * scale.scaleFactor),
        titleLarge = TextStyle(fontSize = 35.sp * scale.scaleFactor),
        titleMedium = TextStyle(fontSize = 20.sp * scale.scaleFactor),
        bodyLarge = TextStyle(fontSize = 16.sp * scale.scaleFactor),
        bodyMedium = TextStyle(fontSize = 14.sp * scale.scaleFactor),
        labelSmall = TextStyle(fontSize = 12.sp * scale.scaleFactor)
    )
}
