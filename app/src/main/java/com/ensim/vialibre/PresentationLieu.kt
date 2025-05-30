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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
import com.ensim.vialibre.ui.components.Titres
import com.ensim.vialibre.ui.theme.ViaLibreTheme

class PresentationLieu : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: "nom inconnu"
        val address = intent.getStringExtra("address") ?: "adresse inconnue"
        val photo = intent.getStringExtra("photoRef")

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
                                title = name,
                                description = address,
                                image = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                targetActivity = MainActivity::class.java
                            )

                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp))

                            Titres("Presentation du lieu")
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