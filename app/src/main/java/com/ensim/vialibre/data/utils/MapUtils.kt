package com.ensim.vialibre.data.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun getLatLngFromPlaceId(context: Context, placeId: String): LatLng? = suspendCancellableCoroutine { cont ->

    if (!Places.isInitialized()) {
        val metaData = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            .metaData
        val apiKey = metaData.getString("com.google.android.geo.API_KEY")
        if (apiKey != null) {
            Places.initialize(context.applicationContext, apiKey)
        } else {
            Log.e("Maps", "Clé API Google Maps manquante.")
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
    }
    val placeFields = listOf(Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.builder(placeId, placeFields).build()

    val placesClient = Places.createClient(context)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            cont.resume(response.place.latLng)
        }
        .addOnFailureListener { exception ->
            Log.e("Maps", "Place not found: ${exception.message}")
            cont.resume(null)
        }
}



