package com.ensim.vialibre.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Titres (titre: String){
    Text(
        text = titre,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 15.dp, top = 15.dp),
        style = MaterialTheme.typography.titleLarge
    )
}