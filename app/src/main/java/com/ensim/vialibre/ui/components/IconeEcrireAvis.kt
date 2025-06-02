package com.ensim.vialibre.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.PresentationLieu

@Composable
fun IconeEcrireAvis (
    context: Context,
    placeName: String,
    placeAdress : String,
    placeImage : String?,
    modifier : Modifier = Modifier,
    targetActivity : Class<*>,
    placeId : String,
    ){
    val intent = Intent(context, targetActivity).apply {
        putExtra("name", placeName)
        putExtra("address", placeAdress)
        putExtra("photoRef", placeImage)
        putExtra("placeId", placeId)
    }
    Column (
        modifier = modifier
            .clickable { context.startActivity(intent) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Icône Edit"
            )
        Text("Donner mon avis")
    }
}