package com.ensim.vialibre.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ensim.vialibre.AffichageCarteActivity

@Composable
fun HomeScreen(onLogout: () -> Unit, context: Context) {
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, AffichageCarteActivity::class.java))
        (context as? Activity)?.finish()
    }
}