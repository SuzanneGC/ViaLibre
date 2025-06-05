package com.ensim.vialibre.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ElementMenu(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick)
            .background(Color.Transparent),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 10.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Divider(
        color = MaterialTheme.colorScheme.primary,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
