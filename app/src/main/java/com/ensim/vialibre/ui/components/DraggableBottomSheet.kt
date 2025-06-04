package com.ensim.vialibre.ui.components

import android.location.Location
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.domain.Lieu
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@Composable
fun DraggableBottomSheet(
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onSearchSubmit: suspend (query : String, lat : Double, lng : Double) -> List<Lieu>?,
    lat: Double?,
    lng:Double?
    ) {
    val scope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxHeightPx = with(LocalDensity.current) { screenHeight.toPx() }
    val minSheetHeightPx = maxHeightPx * 0.2f // hauteur quand réduite (20%)
    val maxSheetHeightPx = maxHeightPx * 1f // hauteur quand étendue (80%)

    val offsetY = remember { Animatable(maxSheetHeightPx - minSheetHeightPx) }

    var query by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<List<Lieu>?>(null) }
    var hasSearched by remember { mutableStateOf(false) }

    Box(modifier.fillMaxSize()) {
        content()

        Box(
            Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { maxSheetHeightPx.toDp() })
                .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                // Snap to min or max selon la position actuelle
                                if (offsetY.value < (maxSheetHeightPx - minSheetHeightPx) / 2) {
                                    offsetY.animateTo(0f) // étendu
                                } else {
                                    offsetY.animateTo(maxSheetHeightPx - minSheetHeightPx) // réduit
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val newOffset = (offsetY.value + dragAmount).coerceIn(
                            0f,
                            maxSheetHeightPx - minSheetHeightPx
                        )
                        scope.launch { offsetY.snapTo(newOffset) }
                    }
                }
                .padding(16.dp)
        ) {
            //sheetContent()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearchSubmit = { query ->
                        hasSearched = true
                        scope.launch {
                            val result = onSearchSubmit(query, lat?: 2.3522, lng?: 2.3522)
                            searchResult = result
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )

                Titres("Résultats :")

                if (!hasSearched) {
                    Text("Aucun résultat pour le moment :( Essayez de chercher un lieu !",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium)
                } else {
                    if (searchResult != null)
                        CustomCardList(items = searchResult!!)
                    else {
                        Text("Oups, aucun lieu trouvé ! Veuillez réessayer",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }

            }

        }
    }
}