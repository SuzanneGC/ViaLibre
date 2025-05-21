package com.ensim.vialibre.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.ui.theme.ViaLibreTheme

@Composable
fun ButtonVL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.secondary, // couleur personnalisée
            contentColor = colorScheme.secondaryContainer
        )
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonVL() {
    ViaLibreTheme(darkTheme = true, dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // ou n’importe quelle couleur
        )
        { ButtonVL(text = "Confirmer", onClick = {}, modifier = Modifier.padding(16.dp)) }
    }
}