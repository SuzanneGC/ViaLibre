package com.ensim.vialibre.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.R
import com.ensim.vialibre.data.repository.Avis
import com.ensim.vialibre.data.utils.StaticStrings
import com.ensim.vialibre.ui.components.molecules.Accordeon
import com.ensim.vialibre.ui.components.atoms.IconeEcrireAvis

@Composable
fun LastAvisScreen(
    placeId: String,
    avis: Avis?,
    onClick: () -> Unit ={}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        StaticStrings.init(LocalContext.current)
        if (avis != null) {
            Accordeon(title = stringResource(id = R.string.dernier_avis_poste),
                modifier = Modifier,
                content = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var texteFormatte = String.format(StaticStrings.criteresAfficher[0], avis.champ1)

                        Text(
                            text = texteFormatte,
                            color = MaterialTheme.colorScheme.primary
                        )

                        texteFormatte = String.format(StaticStrings.criteresAfficher[1], avis.champ2)

                        Text(
                            text = texteFormatte,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        } else {
            Accordeon(title = stringResource(id = R.string.dernier_avis_poste),
                modifier = Modifier,
                content = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        StaticStrings.criteresAfficher.forEach { critere ->
                            val texteFormatte = String.format(critere, "non renseigné")
                            Text(
                                text = texteFormatte,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }

        IconeEcrireAvis(
            modifier = Modifier,
            onClick = {
                onClick()
            },
            placeId = placeId
        )
    }
}
