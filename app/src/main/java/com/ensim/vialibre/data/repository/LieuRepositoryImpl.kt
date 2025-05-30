package com.ensim.vialibre.data.repository

import android.util.Log
import com.ensim.vialibre.domain.Lieu
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LieuRepositoryImpl(private val placesClient: PlacesClient) : LieuRepository {

    override suspend fun searchLieuByName(name: String): Lieu? = withContext(Dispatchers.IO) {
        val TAG = "SearchLieuByName"
        Log.d(TAG, "méthode appelée")
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(name)
            .build()
        try {
            val predictionResponse = placesClient.findAutocompletePredictions(request).await()

            Log.d(TAG, "Mmmh on a initialisé predictionResponse")

            val placeId = predictionResponse.autocompletePredictions.firstOrNull()?.placeId
                ?: return@withContext null

            val placeFields =
                listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHOTO_METADATAS)
            val placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
            val placeResponse = placesClient.fetchPlace(placeRequest).await()
            val place = placeResponse.place

            Log.d(TAG, place.name ?: "Inconnu")

            return@withContext Lieu(
                name = place.name ?: "Inconnu",
                address = place.address ?: "Adresse inconnue",
                photoReference = place.photoMetadatas?.firstOrNull()?.zza()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de la requête: ${e.message}", e)
            return@withContext null
        }
    }
}
