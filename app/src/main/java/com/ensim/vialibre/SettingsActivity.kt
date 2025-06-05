package com.ensim.vialibre

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.data.model.SettingsViewModel
import com.ensim.vialibre.data.model.ThemeViewModel
import com.ensim.vialibre.data.repository.SettingsRepository
import com.ensim.vialibre.ui.accessibility.AppTheme
import com.ensim.vialibre.ui.accessibility.FontSizeScale
import com.ensim.vialibre.ui.components.atoms.ButtonVL
import com.ensim.vialibre.ui.components.atoms.Titres
import com.ensim.vialibre.ui.components.molecules.DeleteAccountButton
import com.ensim.vialibre.ui.components.molecules.Menu
import com.ensim.vialibre.ui.components.molecules.PasswordTextField
import com.ensim.vialibre.ui.components.navigation.HeaderBar
import com.ensim.vialibre.ui.theme.ViaLibreTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel =
                viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val repository = SettingsRepository(applicationContext)
                        @Suppress("UNCHECKED_CAST")
                        return SettingsViewModel(repository) as T
                    }
                })
            val fontSizeScale by settingsViewModel.fontSizeScale.collectAsState()

            val viewModel: ThemeViewModel = viewModel()
            val appTheme by viewModel.theme.collectAsState()

            ViaLibreTheme(
                dynamicColor = false,
                fontSizeScale = fontSizeScale,
                appTheme = appTheme
            ) {
                val context = LocalContext.current
                val navController = rememberNavController()
                var isMenuOpen by remember { mutableStateOf(false) }
                val authViewModel: AuthViewModel = viewModel()
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var error by remember { mutableStateOf<String?>(null) }

                Scaffold(
                    topBar = {
                        HeaderBar(
                            logo = painterResource(id = R.drawable.logovl),
                            onMenuClick = { isMenuOpen = true }
                        )
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        item { Titres("Paramètres") }
                        item { Spacer(Modifier.height(16.dp)) }
                        item {
                            Text(
                                "Taille de la police",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        item { Spacer(Modifier.height(16.dp)) }

                        FontSizeScale.values().forEach { scale ->
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { settingsViewModel.updateFontSize(scale) }
                                        .padding(5.dp)
                                ) {
                                    RadioButton(
                                        selected = fontSizeScale == scale,
                                        onClick = { settingsViewModel.updateFontSize(scale) }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        scale.label,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }


                        item {
                            Text(
                                "Thème de l'application",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        item { Spacer(Modifier.height(16.dp)) }

                        item {
                            Column {
                                AppTheme.entries.forEach { theme ->
                                    var nomTheme = "ERREUR"
                                    if (theme.equals(AppTheme.LIGHT)) {
                                        nomTheme = "Clair"
                                    } else if (theme.equals(AppTheme.DARK)) {
                                        nomTheme = "Sombre"
                                    } else {
                                        nomTheme = "Thème du système"
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.setTheme(theme) }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val selectedTheme by viewModel.theme.collectAsState()

                                        RadioButton(
                                            selected = selectedTheme == theme,
                                            onClick = { viewModel.setTheme(theme) }
                                        )
                                        Text(nomTheme, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }



                        item {
                            ButtonVL(
                                onClick = {
                                    (context as? Activity)?.finish()
                                },
                                text = "Retour",
                            )
                        }

                        item {
                            Spacer(Modifier.height(16.dp))
                        }

                        item { Titres("Supprimer mon compte") }
                        item { Spacer(Modifier.padding(16.dp)) }

                        item {
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = {
                                    Text(
                                        "Email",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                isError = error != null && email.isBlank()
                            )
                        }

                        item { Spacer(modifier = Modifier.height(8.dp)) }

                        item {
                            PasswordTextField(
                                password = password,
                                onPasswordChange = { password = it },
                                isError = error != null && password.isBlank()
                            )
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        item {


                            DeleteAccountButton(
                                onConfirmDelete = {
                                    if (email.isBlank() || password.isBlank()) {
                                        error = "Email ou mot de passe manquant."
                                    } else {
                                        authViewModel.deleteAccount(email!!, password!!,
                                            onSuccess = {
                                                authViewModel.logout()
                                                val intent = Intent(
                                                    context,
                                                    MainActivity::class.java
                                                ).apply {
                                                    flags =
                                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                context.startActivity(intent)
                                            },
                                            onError = { msg ->
                                                error = msg
                                            }
                                        )
                                    }
                                },
                            )

                        }

                        item {
                            error?.let {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(200.dp))
                        }

                    }

                }
                if (isMenuOpen) {
                    Menu(
                        isMenuOpen = true,
                        onCloseMenu = { isMenuOpen = false },
                        authViewModel = authViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}