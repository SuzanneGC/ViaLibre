package com.ensim.vialibre.ui.components.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.R

@Composable
fun IconeEcrireAvis (
    /*context: Context,
    placeName: String,
    placeAdress : String,
    placeImage : String?,*/
    modifier : Modifier = Modifier,
    onClick : () -> Unit,
    //targetActivity : Class<*>,
    placeId : String,
    ){
    /*val intent = Intent(context, targetActivity).apply {
        putExtra("name", placeName)
        putExtra("address", placeAdress)
        putExtra("photoRef", placeImage)
        putExtra("placeId", placeId)
    }*/
    Column (
        modifier = modifier
            .clickable { onClick()/*context.startActivity(intent)*/ }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Icône Edit",
                tint = MaterialTheme.colorScheme.primary
            )
        Text(stringResource(id = R.string.donner_avis), color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium)
    }
}