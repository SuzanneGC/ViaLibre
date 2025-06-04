package com.ensim.vialibre.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email",
                style = MaterialTheme.typography.bodyMedium) },
            isError = error != null && email.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password",
                style = MaterialTheme.typography.bodyMedium) },
            visualTransformation = PasswordVisualTransformation(),
            isError = error != null && email.isBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonVL(onClick = {
            error = null
            if (email.isBlank() || password.isBlank()){error = "L'email et le mot de passe ne peuvent pas être vides"}
            else {
                signInWithEmail(
                    email,
                    password,
                    onSuccess = onLoginSuccess,
                    onError = { error = it })
            }
        },
            text = "Se connecter"
        )
        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red,
                style = MaterialTheme.typography.bodyMedium)
        }
        TextButton(onClick = onNavigateToSignUp) {
            Text("Pas encore de compte ? Inscris-toi",
                style = MaterialTheme.typography.bodyMedium)
        }

    }
}

fun signInWithEmail(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Erreur inconnue")
            }
        }
}

fun isUserLoggedIn(): Boolean {
    return FirebaseAuth.getInstance().currentUser != null
}
