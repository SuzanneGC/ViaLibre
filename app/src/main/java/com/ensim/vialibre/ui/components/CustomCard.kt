package com.ensim.vialibre.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.R
import com.ensim.vialibre.ui.theme.ViaLibreTheme


@Composable
fun CustomCard(
    title: String,
    description: String,
    image: Painter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            // Bande haute avec le titre
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceBright)
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Bande basse avec texte + image superposée
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Texte sur la bande
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.surfaceDim)
                ) {
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                // Image carrée superposée à droite
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCustomCard() {
    ViaLibreTheme(darkTheme = true, dynamicColor = false) {
        CustomCard(
            title = "Ma Carte",
            description = "Texte sur la bande du bas",
            image = painterResource(id = R.drawable.furet), // remplace avec ton image
            modifier = Modifier.padding(16.dp)
        )
    }
}
