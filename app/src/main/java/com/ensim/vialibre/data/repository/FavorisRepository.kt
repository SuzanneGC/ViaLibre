package com.ensim.vialibre.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore


fun addFavori(placeId: String, onResult: (Boolean) -> Unit) {
    val db = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId == null) {
        onResult(false)
        return
    }

    val favori = mapOf(
        "userId" to userId,
        "placeId" to placeId
    )

    // Vérifie s'il est déjà en favori pour ne pas dupliquer
    db.collection("favoris")
        .whereEqualTo("userId", userId)
        .whereEqualTo("placeId", placeId)
        .get()
        .addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                // Pas encore ajouté → on l'ajoute
                db.collection("favoris")
                    .add(favori)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        onResult(false)
                    }
            } else {
                // Déjà en favori → ne rien faire ou renvoyer vrai
                onResult(true)
            }
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(false)
        }
}

fun removeFavori(placeId: String, onResult: (Boolean) -> Unit) {
    val db = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(false)

    db.collection("favoris")
        .whereEqualTo("userId", userId)
        .whereEqualTo("placeId", placeId)
        .get()
        .addOnSuccessListener { snapshot ->
            val batch = db.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    onResult(false)
                }
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(false)
        }
}

fun getFavorisByUser(userId: String, onResult: (List<String>) -> Unit) {
    val db = Firebase.firestore

    db.collection("favoris")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { snapshot ->
            val placeIds = snapshot.documents.mapNotNull { it.getString("placeId") }
            onResult(placeIds)
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(emptyList())
        }
}

fun isLieuFavori(placeId: String, onResult: (Boolean) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(false)
    val db = Firebase.firestore

    db.collection("favoris")
        .whereEqualTo("userId", userId)
        .whereEqualTo("placeId", placeId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            onResult(!querySnapshot.isEmpty)
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(false)
        }
}
