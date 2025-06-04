package com.ensim.vialibre.data.repository

import com.ensim.vialibre.domain.Lieu
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LieuRepositoryImpl(private val placesClient: PlacesClient) : LieuRepository {

    override suspend fun searchLieuByName(
        name: String,
        currentLat: Double,
        currentLng: Double
    ): List<Lieu>? = withContext(Dispatchers.IO) {
        val lieux = mutableListOf<Lieu>()
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(name)
            .setLocationBias(
                RectangularBounds.newInstance(
                    LatLng(currentLat - 0.05, currentLng - 0.05),
                    LatLng(currentLat + 0.05, currentLng + 0.05)
                )
            )
            .build()
        try {
            val predictionResponse = placesClient.findAutocompletePredictions(request).await()
            val predictions = predictionResponse.autocompletePredictions.take(10)

            val placeFields =
                listOf(
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.PHOTO_METADATAS,
                    Place.Field.ID
                )

            predictions.mapNotNull { prediction ->
                try {
                    val placeId = prediction.placeId
                    val placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
                    val placeResponse = placesClient.fetchPlace(placeRequest).await()
                    val place = placeResponse.place
                    lieux.add(
                        Lieu(
                            name = place.name ?: "Inconnu",
                            address = place.address ?: "Adresse inconnue",
                            photoReference = place.photoMetadatas?.firstOrNull()?.zza(),
                            placeId = place.id ?: ""
                        )
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            return@withContext null
        }
        return@withContext lieux
    }

    override suspend fun searchLieuById(placeId: String): Lieu? = withContext(Dispatchers.IO) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.PHOTO_METADATAS
        )
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        try {
            val response = placesClient.fetchPlace(request).await()
            val place = response.place

            val photoRef =
                place.photoMetadatas?.firstOrNull()?.zza()
            return@withContext Lieu(
                name = place.name ?: "Inconnu",
                address = place.address ?: "Adresse inconnue",
                photoReference = place.photoMetadatas?.firstOrNull()?.zza(),
                placeId = place.id ?: ""
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
