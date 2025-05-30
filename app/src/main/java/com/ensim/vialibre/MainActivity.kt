package com.ensim.vialibre

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ensim.vialibre.ui.components.ButtonVL
import com.ensim.vialibre.ui.components.CustomCard
import com.ensim.vialibre.ui.components.HeaderBar
import com.ensim.vialibre.ui.components.Menu
import com.ensim.vialibre.ui.theme.ViaLibreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViaLibreTheme(dynamicColor = false) {
                var isMenuOpen by remember { mutableStateOf(false) }
                val context = LocalContext.current
                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            val logoPainter = painterResource(id = R.drawable.logovl)
                            HeaderBar(
                                logo = logoPainter,
                                onMenuClick = {
                                    isMenuOpen = true
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
                                image = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                targetActivity = PresentationLieu::class.java
                            )

                            Text("Contenu principal")

                            ButtonVL(
                                text = "Confirmer",
                                onClick = {
                                    val intent = Intent(context, AffichageCarte::class.java)
                                    context.startActivity(intent)
                                }
                            )
                        }

                    }
                }
                if (isMenuOpen) {
                    Menu(
                        isMenuOpen = true,
                        onCloseMenu = { isMenuOpen = false },
                    )
                }
            }
        }
    }
}


