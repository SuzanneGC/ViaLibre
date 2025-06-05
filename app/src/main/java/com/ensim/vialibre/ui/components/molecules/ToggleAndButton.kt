package com.ensim.vialibre.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.ui.components.atoms.ButtonVL

@Composable
fun ToggleAndButton(
    numberOfToggles: Int = 1,
    initialToggleStates: List<Boolean> = List(numberOfToggles) { false },
    toggleNames: List<String> = emptyList(),
    onButtonClick: (List<Boolean>) -> Unit
) {
    val toggleStates = remember {
        mutableStateListOf<Boolean>().apply {
            clear()
            addAll(initialToggleStates.take(numberOfToggles))
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        toggleStates.forEachIndexed { index, state ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (toggleNames.isEmpty()) {
                    Text(
                        "Toggle ${index + 1}: ${if (state) "Activé" else "Désactivé"}",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        toggleNames[index],
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = state,
                    onCheckedChange = { toggleStates[index] = it }
                )
            }
        }

        ButtonVL(
            onClick = {
                onButtonClick(toggleStates.toList())
            },
            text = "Confirmer"
        )
    }
}
