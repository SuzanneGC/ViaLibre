package com.ensim.vialibre.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ensim.vialibre.data.repository.Avis
import com.ensim.vialibre.data.repository.getUserAvisForPlace
import com.ensim.vialibre.data.repository.setAvis
import com.ensim.vialibre.ui.components.Accordeon
import com.ensim.vialibre.ui.components.ButtonVL
import com.ensim.vialibre.ui.components.ToggleAndButton

@Composable
fun SetAvisScreen(
    placeId: String,
    onClick: () -> Unit ={}
) {
    val context = LocalContext.current
    var initialAvis by remember { mutableStateOf<Avis?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(placeId) {
        initialAvis = getUserAvisForPlace(placeId)
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Accordeon(
                title = "Changer son avis",
                modifier = Modifier,
                content = {
                    ToggleAndButton(
                        numberOfToggles = 2,
                        initialToggleStates = listOf(
                            initialAvis?.champ1 ?: false,
                            initialAvis?.champ2 ?: false
                        )
                    ) { toggleValues ->
                        val newAvis = Avis(
                            champ1 = toggleValues[0],
                            champ2 = toggleValues[1],
                            placeId = placeId
                        )
                        setAvis(newAvis) {
                            Log.d("Envoi avis", "Avis posté")
                            Toast.makeText(context, "Avis envoyé !", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            ButtonVL(
                onClick = { onClick() },
                text = "Valider et retourner en arrière",
                modifier = Modifier
            )
        }
    }
}
    /*Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Accordeon(title = "Changer son avis",
            modifier = Modifier,
            content = {
                ToggleAndButton (numberOfToggles = 2){ toggleValues ->
                    val newAvis: Avis =
                        Avis(
                            champ1 = toggleValues[0],
                            champ2 = toggleValues[1],
                            placeId = placeId)
                    setAvis(newAvis, { Log.d("Envoi avis", "Avis posté") })
                    Toast.makeText(context, "Avis envoyé !", Toast.LENGTH_SHORT).show()
                }
            }
        )
        ButtonVL(
            onClick = {
                onClick()
            },
            text = "Valider et retourner en arrière",
            modifier = Modifier
        )

    }
}*/