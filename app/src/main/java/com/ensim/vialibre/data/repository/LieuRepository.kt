package com.ensim.vialibre.data.repository

import com.ensim.vialibre.domain.Lieu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface LieuRepository {
    suspend fun searchLieuByName(name: String, currentLat: Double = 48.8566, currentLng: Double = 2.3522): List<Lieu>?
    suspend fun searchLieuById(placeId: String): Lieu?
}
