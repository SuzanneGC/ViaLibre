package com.ensim.vialibre.data.repository

import com.ensim.vialibre.domain.Lieu

interface LieuRepository {
    suspend fun searchLieuByName(name: String, currentLat: Double = 48.8566, currentLng: Double = 2.3522): List<Lieu>?
}
