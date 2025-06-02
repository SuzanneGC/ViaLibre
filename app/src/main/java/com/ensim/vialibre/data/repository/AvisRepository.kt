package com.ensim.vialibre.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.tasks.await

data class Avis(
    var champ1: Boolean = true,
    val placeId: String = ""
)

fun getAvisById(avisId: String, onResult: (Avis?) -> Unit) {

            onResult(null)
    val db = Firebase.firestore
    val docRef = db.collection("avis").document(avisId)

    docRef.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val avis = document.toObject(Avis::class.java)
                onResult(avis)
            } else {
                onResult(null)
            }
        }
        .addOnFailureListener {
            it.printStackTrace()
        }
}


suspend fun getAvisByPlaceId(placeId: String): List<Avis> {
    Log.d("PresentationLieu", "getAvisByPlaceId appelé avec placeId = $placeId")
    val db = Firebase.firestore
    return try {

        val querySnapshot = db.collection("avis")
            .whereEqualTo("placeId", placeId)
            .get()
            .await()
        val avisList = querySnapshot.documents.mapNotNull { it.toObject(Avis::class.java) }
        Log.d("PresentationLieu", "Avis récupérés, count = ${avisList.size}")
        avisList
    } catch (e: Exception) {
        Log.d("PresentationLieu", "problème dans getAvisByPlaceId mon cap'taine : " + e.message)
        e.printStackTrace()
        emptyList()
    }
}


fun setAvis(avis: Avis, onResult: (Boolean) -> Unit) {
    val db = Firebase.firestore
    db.collection("avis")
        .add(avis)
        .addOnSuccessListener {
            onResult(true)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            onResult(false)
        }
}
