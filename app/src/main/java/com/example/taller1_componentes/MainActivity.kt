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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taller1_componentes.game.GameEngine
import com.example.taller1_componentes.sensors.OrientationSensor
import com.example.taller1_componentes.ui.theme.Taller1_ComponentesTheme
import kotlinx.coroutines.delay

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

    val context = LocalContext.current
    val sensor = remember {
        OrientationSensor(context = context)
    }

    val game = remember {
        GameEngine()
    }

    if (!jugando) {
        PantallaInicio(
            onEmpezar = {
                game.startGame()
                jugando = true
            }
        )
    } else {
        PantallaJuego(
            sensor = sensor,
            game = game
        )
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
fun PantallaJuego(
    sensor: OrientationSensor,
    game: GameEngine
) {

    var direccion by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {

        sensor.start()

        onDispose {
            sensor.stop()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            direccion = sensor.direction
            delay(100)
        }
    }

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
            text = "Dirección actual: $direccion°",
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Dirección escondida: ${game.targetDirection}°",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}