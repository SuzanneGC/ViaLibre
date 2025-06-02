package com.ensim.vialibre.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.PresentationLieu
import com.ensim.vialibre.domain.Lieu

@Composable
fun CustomCardList(
    items: List<Lieu>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items) { item ->
            Log.d("CardList", "ID : " + item.placeId)
            CustomCard(
                title = item.name,
                description = item.address,
                image = item.photoReference,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                targetActivity = PresentationLieu::class.java,
                placeId = item.placeId,

            )
        }
    }
}