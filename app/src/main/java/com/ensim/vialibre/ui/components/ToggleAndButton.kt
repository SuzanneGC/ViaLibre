package com.ensim.vialibre.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToggleAndButton(
    initialToggleState: Boolean = false,
    onButtonClick: (Boolean) -> Unit
) {
    var toggleState by remember { mutableStateOf(initialToggleState) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = toggleState,
                onCheckedChange = { toggleState = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (toggleState) "Activé" else "Désactivé")
        }

        Button(onClick = {
            onButtonClick(toggleState)
        }) {
            Text("Confirmer")
        }
    }
}
