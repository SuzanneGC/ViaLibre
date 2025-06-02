package com.ensim.vialibre.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(
    navController: NavHostController,
    onMenuClick: () -> Unit,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn()) "home" else "login"
    ) {
        composable("login") {
            Login(onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                })
        }
        composable("signup") { SignUp(onSignUpSuccess = { navController.navigate("home") }) }
        composable("home") {
            HomeScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                context = context
            )
        }
    }
}
