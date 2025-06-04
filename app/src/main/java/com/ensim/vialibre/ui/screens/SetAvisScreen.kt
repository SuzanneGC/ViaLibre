package com.ensim.vialibre.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ensim.vialibre.data.repository.Avis
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
    Column(
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
}