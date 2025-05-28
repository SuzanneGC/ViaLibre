package com.ensim.vialibre

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.ui.components.ButtonVL
import com.ensim.vialibre.ui.components.CustomCard
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.theme.ViaLibreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViaLibreTheme(dynamicColor = false) {
                val context = LocalContext.current
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
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomCard(
                            title = "Ma Carte",
                            description = "Texte sur la bande du bas",
                            image = painterResource(id = R.drawable.furet),
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentHeight(),

                        )

                        Text("Contenu principal")

                        ButtonVL(
                            text = "Confirmer",
                            onClick = {
                                val intent = Intent(context, AffichageCarte::class.java)
                                context.startActivity(intent)}
                        )
                    }
                }
            }
        }
    }
}

