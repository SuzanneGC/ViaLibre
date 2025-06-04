package com.ensim.vialibre.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensim.vialibre.R
import com.ensim.vialibre.data.repository.Avis
import com.ensim.vialibre.ui.components.Accordeon
import com.ensim.vialibre.ui.components.IconeEcrireAvis

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
                        Text(stringResource(id = R.string.crit1, avis.champ1), color = MaterialTheme.colorScheme.primary)
                        Text(stringResource(id = R.string.crit2, avis.champ2), color = MaterialTheme.colorScheme.primary)
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
                        Text(stringResource(id = R.string.crit1, "non renseigné"), color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium)
                        Text(stringResource(id = R.string.crit2, "non renseigné"), color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium)
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
