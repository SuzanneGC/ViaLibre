package com.ensim.vialibre

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ensim.vialibre.data.model.AuthViewModel
import com.ensim.vialibre.ui.components.AppNavHost
import com.ensim.vialibre.ui.components.ButtonVL
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.components.HomeScreen
import com.ensim.vialibre.ui.components.Login
import com.ensim.vialibre.ui.components.Menu
import com.ensim.vialibre.ui.components.isUserLoggedIn
import com.ensim.vialibre.ui.theme.ViaLibreTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            ViaLibreTheme(dynamicColor = false) {

                val context = LocalContext.current
                val navController = rememberNavController()
                var isMenuOpen by remember { mutableStateOf(false) }
                val authViewModel: AuthViewModel = viewModel()

                Scaffold(
                    topBar = {
                        HeaderBar(
                            logo = painterResource(id = R.drawable.logovl),
                            onMenuClick = { isMenuOpen = true }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(
                            navController = navController,
                            onMenuClick = { isMenuOpen = true },
                            context = context
                        )

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
    }
}
