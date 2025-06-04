package com.ensim.vialibre.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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

/*
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
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            )
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Image(
            painter = logo,
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 16.dp)
                .height(40.dp)
        )
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}*/

@Composable
fun HeaderBar(
    onMenuClick: () -> Unit,
    logo: Painter,
    modifier: Modifier = Modifier
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(top = statusBarHeight)
    ) {
        Image(
            painter = logo,
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .height(40.dp)
        )
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}



