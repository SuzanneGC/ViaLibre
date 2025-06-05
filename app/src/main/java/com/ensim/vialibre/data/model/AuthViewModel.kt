package com.ensim.vialibre.data.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ensim.vialibre.data.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel (private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {
    val errorMessage = mutableStateOf<String?>(null)
    val accountDeleted = mutableStateOf(false)

    private val _isUserAuthenticated =
        MutableStateFlow(FirebaseAuth.getInstance().currentUser != null)

    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated.asStateFlow()

    init {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            _isUserAuthenticated.value = auth.currentUser != null
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun deleteAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, password)

        if (user == null) {
            onError("Utilisateur introuvable.")
            return
        }

        user?.reauthenticate(credential)?.addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                user.delete().addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        onSuccess()
                    } else {
                        val message = when (val ex = deleteTask.exception) {
                            is FirebaseAuthException -> "Erreur Firebase : ${ex.message}"
                            is FirebaseNetworkException -> "Problème de connexion. Vérifie ta connexion internet."
                            else -> "Échec de la suppression du compte."
                        }
                        onError(message)
                    }
                }
            } else {
                val message = when (val ex = authTask.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Email ou mot de passe incorrect."
                    is FirebaseAuthInvalidUserException -> "Aucun compte ne correspond à ces informations."
                    is FirebaseNetworkException -> "Impossible de se connecter au serveur. Vérifie ta connexion."
                    else -> "Échec de l’authentification : ${ex?.localizedMessage ?: "erreur inconnue"}"
                }
                onError(message)
            }
        }
    }

}
