package com.ensim.vialibre.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.R
import com.ensim.vialibre.ui.theme.ViaLibreTheme

@Composable
fun HeaderBar(
    onMenuClick: () -> Unit,
    logo: Painter,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Logo à gauche
        Image(
            painter = logo,
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 16.dp)
                .height(40.dp)
        )

        // Icône menu à droite
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.surfaceDim
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HeaderBarPreview() {
    ViaLibreTheme(darkTheme = true, dynamicColor = false) {
        HeaderBar(
            onMenuClick = { /* menu click preview */ },
            logo = painterResource(id = R.drawable.logovl) // remplace par ton logo
        )
    }
}
