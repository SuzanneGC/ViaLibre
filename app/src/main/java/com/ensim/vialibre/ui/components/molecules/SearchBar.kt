package com.ensim.vialibre.ui.components.molecules

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.defaultMinSize(minHeight = 0.dp),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text("Rechercher un lieu",
            style = MaterialTheme.typography.bodyMedium) },
        trailingIcon = {
            IconButton(
                onClick = { onSearchSubmit(query) })
            {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Icône recherche"
                )
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}