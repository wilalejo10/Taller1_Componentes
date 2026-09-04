package com.example.taller1_componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taller1_componentes.ui.theme.Taller1_ComponentesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Taller1_ComponentesTheme {
                EscondidasApp()
            }
        }
    }
}

@Composable
fun EscondidasApp() {

    var jugando by remember { mutableStateOf(false) }

    if (!jugando) {
        PantallaInicio(
            onEmpezar = {
                jugando = true
            }
        )
    } else {
        PantallaJuego()
    }
}

@Composable
fun PantallaInicio(
    onEmpezar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "ESCONDIDAS",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Encuentra la dirección escondida",
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Mueve y gira tu celular para buscar la dirección correcta.\n\n" +
                    "Verás si estás Frío, Tibio o Caliente según qué tan cerca estés.",
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = onEmpezar,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text("EMPEZAR")
        }
    }
}

@Composable
fun PantallaJuego() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Juego iniciado",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Aquí vamos a poner los sensores y la dirección.",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}