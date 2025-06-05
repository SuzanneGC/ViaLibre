package com.ensim.vialibre.ui.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.ensim.vialibre.ui.components.atoms.ButtonVL

@Composable
fun DeleteAccountButton(
    onConfirmDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        ButtonVL(
            onClick = { showDialog = true },
            text = "Supprimer mon compte"
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmer la suppression",
                    style = MaterialTheme.typography.titleMedium) },
                text = {
                    Text("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.",
                        style = MaterialTheme.typography.bodyMedium)
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        onConfirmDelete()
                    }) {
                        Text("Supprimer", color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Annuler",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                textContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}
