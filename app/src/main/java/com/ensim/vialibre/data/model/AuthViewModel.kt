package com.ensim.vialibre.data.model

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _isUserAuthenticated = MutableStateFlow(FirebaseAuth.getInstance().currentUser != null)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated.asStateFlow()

    init {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            _isUserAuthenticated.value = auth.currentUser != null
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
