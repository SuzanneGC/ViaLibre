package com.ensim.vialibre.data.repository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Place(
    val id: String = "",
    val nom: String = ""
)

fun getPlaceById(placeId: String, onResult: (Place?) -> Unit) {
    val db = Firebase.firestore
    val docRef = db.collection("places").document(placeId)

    docRef.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val place = document.toObject(Place::class.java)
                onResult(place)
            } else {
                onResult(null)
            }
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(null)
        }
}

fun getPlaceWithAvis(placeId: String, onResult: (Place, List<Avis>) -> Unit) {
    val db = Firebase.firestore

    db.collection("places").document(placeId).get()
        .addOnSuccessListener { placeDoc ->
            val place = placeDoc.toObject(Place::class.java)?.copy(id = placeDoc.id)

            if (place != null) {
                db.collection("avis")
                    .whereEqualTo("placeId", placeId)
                    .get()
                    .addOnSuccessListener { avisSnapshot ->
                        val avisList = avisSnapshot.documents.mapNotNull {
                            it.toObject(Avis::class.java)
                        }
                        onResult(place, avisList)
                    }
            }
        }
}

