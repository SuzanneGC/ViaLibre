package com.ensim.vialibre.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.tasks.await

data class Avis(
    var champ1: Boolean = true,
    var champ2 : Boolean = true,
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
    /*db.collection("avis")
        .add(avis)
        .addOnSuccessListener {
            onResult(true)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            onResult(false)
        }*/
    val avisMap = avis.toMap().toMutableMap()

    // Ajout de la date actuelle
    avisMap["date"] = FieldValue.serverTimestamp()

    db.collection("avis")
        .add(avisMap)
        .addOnSuccessListener {
            onResult(true)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            onResult(false)
        }
}


suspend fun getLastAvisById(placeId: String):Avis? {

    val snapshot=Firebase.firestore
        .collection("avis")
        .whereEqualTo("placeId",placeId)
        .orderBy("date",Query.Direction.DESCENDING)
        .limit(1)
        .get()
        .await()
    Log.d("LastAvis", "Dans la méthode : $snapshot.documents.firstOrNull()?.toObject(Avis::class.java)")

    return snapshot.documents.firstOrNull()?.toObject(Avis::class.java)

    /*val db = Firebase.firestore
    //val docRef = db.collection("avis")//.document(avisId)

    //docRef.orderBy("date", Query.Direction.DESCENDING)
    db.collection("avis")
        .whereEqualTo("placeId", placeId)
        .orderBy("date", Query.Direction.DESCENDING)
        .limit(1)
        .get()
        .addOnSuccessListener { /*document ->
            if (document.exists()) {
                val avis = document.toObject(Avis::class.java)
                onResult(avis)
            } else {
                onResult(null)
            }*/
                querySnapshot ->
            val document = querySnapshot.documents.firstOrNull()
            val avis = document?.toObject(Avis::class.java)
            Log.d("LastAvis", "Dans la méthode : $avis")
            onResult(avis)

        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(null)
        }*/
}

fun Avis.toMap(): Map<String, Any?> {
    return mapOf(
        "champ1" to champ1,
        "champ2" to champ2,
        "placeId" to placeId
        // N’inclue pas la date ici
    )
}
