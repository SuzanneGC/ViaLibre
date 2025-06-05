package com.ensim.vialibre.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.data.repository.AuthRepository
import com.ensim.vialibre.ui.components.atoms.ButtonVL
import com.ensim.vialibre.ui.components.atoms.Titres
import com.ensim.vialibre.ui.components.molecules.PasswordTextField

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmerPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var mdpDifferentsError by remember { mutableStateOf(false) }

    val authRepository = remember { AuthRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Titres("S'inscrire")
        Spacer(Modifier.padding(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            password = password,
            onPasswordChange = {
                password = it
                if (mdpDifferentsError) {
                    mdpDifferentsError = false
                }
            },
            isError = password.isBlank()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            password = confirmerPassword,
            onPasswordChange = {
                confirmerPassword = it
                if (mdpDifferentsError) {
                    mdpDifferentsError = false
                }
            },
            isError = !password.equals(confirmerPassword)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonVL(
            text = "S'inscrire",
            onClick = {
                if (password.equals(confirmerPassword)) {
                    authRepository.signUpWithEmail(email, password) { success, error ->
                        if (success) {
                            onSignUpSuccess()
                        } else {
                            errorMessage = error
                        }
                    }
                } else {
                    mdpDifferentsError = true
                }
            })

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }
        if (mdpDifferentsError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Les deux mots de passe sont différents.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
