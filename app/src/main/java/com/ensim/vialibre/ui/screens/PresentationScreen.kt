package com.ensim.vialibre.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.PresentationLieuActivity
import com.ensim.vialibre.R
import com.ensim.vialibre.data.repository.addFavori
import com.ensim.vialibre.data.repository.isLieuFavori
import com.ensim.vialibre.data.repository.removeFavori
import com.ensim.vialibre.ui.components.atoms.ButtonVL
import com.ensim.vialibre.ui.components.atoms.CustomCard
import com.ensim.vialibre.ui.components.molecules.MapCenteredOnPlace
import com.ensim.vialibre.ui.components.atoms.Titres
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun PresentationScreen(
    name: String,
    address: String,
    photo: String?,
    placeId: String?,
    latLngState: State<LatLng?>,
    modifier: Modifier
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            CustomCard(
                title = name,
                description = address,
                image = photo,
                placeId = placeId,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                targetActivity = PresentationLieuActivity::class.java,
                clickable = false
            )
        }

        item {
            Divider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item { Titres(stringResource(id = R.string.titre_presentation_lieu)) }

        item {
            latLngState.value?.let { latLng ->
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(latLng, 15f)
                }
                MapCenteredOnPlace(latLng = latLng,
                    lieu = name)
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        item {
            var isFavori by remember { mutableStateOf<Boolean?>(null) }

            LaunchedEffect(placeId) {
                if (!placeId.isNullOrEmpty()) {
                    isLieuFavori(placeId) { result ->
                        isFavori = result
                    }
                }
            }

            when (isFavori) {
                null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                true -> {
                    ButtonVL("Retirer des favoris",
                        {
                            if (placeId != null) {
                                removeFavori(placeId) {
                                    isFavori = false
                                }
                            }
                        })

                }

                false -> {
                    ButtonVL("Ajouter aux favoris", {
                        if (placeId != null) {
                            addFavori(placeId) {
                                isFavori = true
                            }
                        }
                    }
                    )
                }
            }
        }

    }
}