package com.ensim.vialibre.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.PresentationLieu
import com.ensim.vialibre.R
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
    ){
        items(items){item ->
            CustomCard(
                title = item.name,
                description = item.address,
                image = item.photoReference,
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight(),
                targetActivity = PresentationLieu::class.java

            )
        }
    }
}