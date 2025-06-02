package com.ensim.vialibre.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.AffichageCarte
import com.ensim.vialibre.PresentationLieu
import com.ensim.vialibre.data.repository.Place
import com.ensim.vialibre.data.repository.getPlaceById

@Composable
fun HomeScreen(onLogout: () -> Unit, context: Context) {

    var place by remember { mutableStateOf<Place?>(null) }
    var placeId : String = "xNa4tbMCDVxj8P1tlyRh"

    LaunchedEffect(placeId) {
        getPlaceById(placeId) {
            place = it
        }
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomCard(
            title = "Ma Carte",
            description = "Texte sur la bande du bas",
            image = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            targetActivity = PresentationLieu::class.java,
            placeId = placeId
        )

        Text("Contenu principal")

        ButtonVL(
            text = "Confirmer",
            onClick = {
                val intent = Intent(context, AffichageCarte::class.java)
                context.startActivity(intent)
            }
        )

        Text("Nom du lieu : " + (place?.nom ?: "INCONNU"))


        Button(onClick = onLogout) {
            Text("Se déconnecter")
        }


    }

}