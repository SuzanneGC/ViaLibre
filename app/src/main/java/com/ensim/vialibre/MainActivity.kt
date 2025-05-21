package com.ensim.vialibre

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.theme.ViaLibreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViaLibreTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val logoPainter = painterResource(id = R.drawable.logovl)
                        HeaderBar(
                            logo = logoPainter,
                            onMenuClick = {
                                // Action menu (ici un Toast pour l'exemple)
                                Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                ) { innerPadding ->
                    // Contenu principal en dessous du header
                    Box(modifier = Modifier.padding(innerPadding)) {
                        // Exemple contenu
                        Text("Contenu principal", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    ViaLibreTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val logoPainter = painterResource(id = android.R.drawable.sym_def_app_icon)
                HeaderBar(
                    logo = logoPainter,
                    onMenuClick = { /* preview click action */ }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Text("Contenu principal", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
