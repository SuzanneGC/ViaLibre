package com.ensim.vialibre.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.data.repository.Avis
import com.google.android.gms.maps.model.LatLng

@Composable
fun ScreenCarousel(
    modifier: Modifier,
    name: String,
    address: String,
    photo: String?,
    placeId: String,
    latLngState: State<LatLng?>,
    avis: Avis?
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    var isScreenLastAvis by remember { mutableStateOf(true) }

    Column(modifier = modifier) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> PresentationScreen(
                    name = name,
                    address = address,
                    photo = photo,
                    placeId = placeId,
                    latLngState = latLngState,
                    modifier = Modifier
                )

                1 -> if (isScreenLastAvis)
                    LastAvisScreen(
                        placeId = placeId,
                        avis = avis,
                        onClick = { isScreenLastAvis = false }
                    )
                else SetAvisScreen(
                    placeId = placeId,
                    onClick = { isScreenLastAvis = true }
                )
            }
        }
    }
}
