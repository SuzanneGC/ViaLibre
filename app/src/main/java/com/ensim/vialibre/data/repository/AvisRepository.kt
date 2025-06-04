package com.ensim.vialibre.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class Avis(
    var champ1: Boolean = true,
    var champ2: Boolean = true,
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

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val avisMap = avis.toMap().toMutableMap()

    // Ajout de la date actuelle et de l'id de l'utilisateur
    avisMap["date"] = FieldValue.serverTimestamp()
    avisMap["userId"] = userId

    db.collection("avis")
        .whereEqualTo("placeId", avis.placeId)
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                // Un avis existe déjà : on met à jour le premier document trouvé
                val docId = querySnapshot.documents[0].id
                db.collection("avis").document(docId)
                    .set(avisMap)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        onResult(false)
                    }
            } else {
                // Aucun avis existant : on ajoute un nouveau document
                db.collection("avis")
                    .add(avisMap)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        onResult(false)
                    }
            }
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            onResult(false)
        }
}


suspend fun getLastAvisById(placeId: String): Avis? {

    val snapshot = Firebase.firestore
        .collection("avis")
        .whereEqualTo("placeId", placeId)
        .orderBy("date", Query.Direction.DESCENDING)
        .limit(1)
        .get()
        .await()
    Log.d(
        "LastAvis",
        "Dans la méthode : $snapshot.documents.firstOrNull()?.toObject(Avis::class.java)"
    )

    return snapshot.documents.firstOrNull()?.toObject(Avis::class.java)
}

fun Avis.toMap(): Map<String, Any?> {
    return mapOf(
        "champ1" to champ1,
        "champ2" to champ2,
        "placeId" to placeId
        // N’inclue pas la date ici
    )
}

suspend fun getAllAvisUser(userId: String): List<Avis> {

    val db = Firebase.firestore
    return try {

        val querySnapshot = db.collection("avis")
            .whereEqualTo("userId", userId)
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

suspend fun getUserAvisForPlace(placeId: String): Avis? = suspendCoroutine { cont ->
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId == null) {
        cont.resume(null)
        return@suspendCoroutine
    }

    Firebase.firestore.collection("avis")
        .whereEqualTo("placeId", placeId)
        .whereEqualTo("userId", userId)
        .limit(1)
        .get()
        .addOnSuccessListener { result ->
            val doc = result.firstOrNull()
            if (doc != null) {
                val avis = Avis(
                    placeId = placeId,
                    champ1 = doc.getBoolean("champ1") ?: false,
                    champ2 = doc.getBoolean("champ2") ?: false,
                )
                cont.resume(avis)
            } else {
                cont.resume(null)
            }
        }
        .addOnFailureListener {
            cont.resume(null)
        }
}

