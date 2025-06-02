package com.ensim.vialibre.domain

data class Lieu(
    val placeId : String,
    val name: String,
    val address: String,
    val photoReference: String? = null
)
