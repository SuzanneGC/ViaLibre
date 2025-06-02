package com.ensim.vialibre.data.repository

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
}
