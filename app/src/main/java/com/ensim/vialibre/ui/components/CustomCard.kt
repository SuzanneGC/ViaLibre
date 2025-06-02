package com.ensim.vialibre.ui.components

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ensim.vialibre.R


@Composable
fun CustomCard(
    title: String,
    description: String,
    image: String?,
    modifier: Modifier = Modifier,
    targetActivity: Class<*>,
    placeId: String?
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, targetActivity).apply {
                    putExtra("name", title)
                    putExtra("address", description)
                    putExtra("photoRef", image)
                    putExtra("placeId", placeId)
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceDim),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                )

                SquareCenteredImage(
                    image = image,
                    modifier = Modifier
                        .size(80.dp),
                    titre = title,
                    context = context
                )
            }
        }
    }
}

@Composable
fun SquareCenteredImage(
    image: String?,
    titre: String?,
    modifier: Modifier = Modifier,
    context: Context
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.surfaceVariant) // Optionnel : fond
    ) {

        AsyncImage(
            model = image?.let { buildPhotoUrl(context, image) },
            contentDescription = titre,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.furet), // image locale par défaut
            error = painterResource(id = R.drawable.furet), // si erreur de chargement
            contentScale = ContentScale.Crop,
        )
    }
}

fun buildPhotoUrl(context: Context, photoReference: String): String {
    val apiKey = context
        .packageManager
        .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        .metaData
        .getString("com.google.android.geo.API_KEY")
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=$apiKey"
}
