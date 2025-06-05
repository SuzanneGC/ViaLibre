package com.ensim.vialibre.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUpWithEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }


    fun deleteUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val credential =
            EmailAuthProvider.getCredential(email, password) // à redemander à l'utilisateur

        user?.reauthenticate(credential)
            ?.addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    // Maintenant qu’il est re-authentifié, on peut supprimer
                    user.delete()
                        .addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                onSuccess()
                            } else {
                                onError(deleteTask.exception?.message ?: "Erreur de suppression")
                            }
                        }
                } else {
                    onError(authTask.exception?.message ?: "Échec de re-authentification")
                }
            }

    }
}
