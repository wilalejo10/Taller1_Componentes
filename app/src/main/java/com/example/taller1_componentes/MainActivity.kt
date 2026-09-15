package com.example.taller1_componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import com.example.taller1_componentes.game.Proximidad
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
            game = game,
            onReiniciarApp = {
                jugando = false
            }
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
    game: GameEngine,
    onReiniciarApp: () -> Unit
) {

    var direccion by remember { mutableStateOf(0) }
    var tiempo by remember { mutableStateOf(0) }
    var tiempoRestante by remember { mutableStateOf(60) }

    DisposableEffect(Unit) {
        sensor.start()
        onDispose {
            sensor.stop()
        }
    }

    val estado = remember(direccion) {
        Proximidad.calcular(direccion, game.targetDirection)
    }

    val juegoFinalizado = estado.encontrado || tiempoRestante <= 0

    // Lectura del sensor
    LaunchedEffect(juegoFinalizado) {
        while (!juegoFinalizado) {
            direccion = sensor.direction
            delay(100)
        }
    }

    // Cronómetro de tiempo transcurrido
    LaunchedEffect(juegoFinalizado) {
        while (!juegoFinalizado) {
            delay(1000)
            tiempo++
        }
    }

    // Cronómetro regresivo
    LaunchedEffect(juegoFinalizado) {
        while (!juegoFinalizado) {
            delay(1000)
            tiempoRestante--
        }
    }

    // 1 y 2. Detectar cuando finaliza (por encontrar o tiempo agotado) y calcular métricas
    LaunchedEffect(juegoFinalizado) {
        if (juegoFinalizado) {
            game.calcularResultados(tiempoRestante = tiempoRestante, direccionActual = direccion)
        }
    }

    val seAcaboElTiempo = tiempoRestante <= 0 && !estado.encontrado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(estado.color)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Juego iniciado",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Tiempo: ${tiempo}s",
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Tiempo restante: ${tiempoRestante}s",
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Dirección actual: $direccion°",
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Dirección escondida: ${game.targetDirection}°",
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = estado.mensaje,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 24.dp)
        )

        // Muestra de métricas al ganar
        if (estado.encontrado) {
            Text(
                text = "¡Juego terminado! Encontraste la dirección",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Tiempo empleado: ${game.tiempoEmpleado}s",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Precisión: ${game.precision}%",
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Puntaje total: ${game.puntaje} pts",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Muestra de estado al perder
        if (seAcaboElTiempo) {
            Text(
                text = "Se acabó el tiempo, no lo encontraste",
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Precisión: ${game.precision}%",
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Puntaje total: ${game.puntaje} pts",
                modifier = Modifier.padding(top = 4.dp)
            )
        }


        if (juegoFinalizado) {
            Button(
                onClick = {
                    game.reiniciarJuego()
                    tiempo = 0
                    tiempoRestante = 60
                    direccion = sensor.direction // Lee la dirección actual del sensor de inmediato
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("REINICIAR JUEGO")
            }
        }
    }
}