package com.ensim.vialibre.data.repository

import com.ensim.vialibre.domain.Lieu

interface LieuRepository {
    suspend fun searchLieuByName(name: String): List<Lieu>?
}
