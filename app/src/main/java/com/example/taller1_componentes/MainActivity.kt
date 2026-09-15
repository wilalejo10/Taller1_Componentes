package com.example.taller1_componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taller1_componentes.game.GameEngine
import com.example.taller1_componentes.game.Proximidad
import com.example.taller1_componentes.sensors.OrientationSensor
import com.example.taller1_componentes.ui.theme.Taller1_ComponentesTheme
import kotlinx.coroutines.delay

enum class Pantalla { DASHBOARD, COMO_JUGAR, JUEGO, RESULTADO }

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
    var pantallaActual by remember { mutableStateOf(Pantalla.DASHBOARD) }
    var mejorPuntaje by remember { mutableStateOf(0) }
    var mejorTiempo by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val sensor = remember { OrientationSensor(context) }
    val game = remember { GameEngine() }

    when (pantallaActual) {
        Pantalla.DASHBOARD -> PantallaDashboard(
            mejorPuntaje = mejorPuntaje,
            mejorTiempo = mejorTiempo,
            onIniciar = {
                game.startGame()
                pantallaActual = Pantalla.JUEGO
            },
            onComoJugar = { pantallaActual = Pantalla.COMO_JUGAR }
        )
        Pantalla.COMO_JUGAR -> PantallaComoJugar(
            onVolver = { pantallaActual = Pantalla.DASHBOARD }
        )
        Pantalla.JUEGO -> PantallaJuego(
            sensor = sensor,
            game = game,
            onTerminar = {
                if (game.puntaje > mejorPuntaje) mejorPuntaje = game.puntaje
                if (mejorTiempo == 0 || (game.tiempoEmpleado < mejorTiempo && game.precision > 0)) {
                    mejorTiempo = game.tiempoEmpleado
                }
                pantallaActual = Pantalla.RESULTADO
            }
        )
        Pantalla.RESULTADO -> PantallaResultado(
            game = game,
            mejorTiempo = mejorTiempo,
            onReiniciar = {
                game.reiniciarJuego()
                pantallaActual = Pantalla.JUEGO
            },
            onVolverMenu = { pantallaActual = Pantalla.DASHBOARD }
        )
    }
}

@Composable
fun PantallaDashboard(
    mejorPuntaje: Int,
    mejorTiempo: Int,
    onIniciar: () -> Unit,
    onComoJugar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("CALIENTE / FRÍO", style = MaterialTheme.typography.headlineLarge)
        Text("¡Encuéntralo!", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 24.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Mejor Tiempo: ${if (mejorTiempo > 0) "${mejorTiempo}s" else "--"}")
                Text("Mejor Puntuación: $mejorPuntaje pts")
            }
        }

        Button(onClick = onIniciar, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("NUEVA PARTIDA")
        }
        OutlinedButton(onClick = onComoJugar, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("CÓMO JUGAR")
        }
    }
}

@Composable
fun PantallaComoJugar(onVolver: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¿Cómo Jugar?", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "1. El personaje se esconderá en un punto cardinal aleatorio (0° a 360°).\n" +
                    "2. Gira y mueve tu teléfono para buscar la dirección objetivo.\n" +
                    "3. El color de la pantalla cambiará según la distancia:\n" +
                    "   • Azul: Frío\n   • Amarillo: Tibio\n   • Rojo: Caliente\n   • Verde: ¡Encontrado!\n" +
                    "4. Encuéntralo antes de que se agote el tiempo para obtener la máxima puntuación.",
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onVolver) { Text("ENTENDIDO") }
    }
}

@Composable
fun PantallaJuego(
    sensor: OrientationSensor,
    game: GameEngine,
    onTerminar: () -> Unit
) {
    var direccion by remember { mutableStateOf(0) }
    var tiempoRestante by remember { mutableStateOf(game.tiempoTotal) }

    DisposableEffect(Unit) {
        sensor.start()
        onDispose { sensor.stop() }
    }

    LaunchedEffect(Unit) {
        while (tiempoRestante > 0) {
            delay(1000)
            tiempoRestante--
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            direccion = sensor.direction
            delay(100)
        }
    }

    val estado = remember(direccion) { Proximidad.calcular(direccion, game.targetDirection) }

    LaunchedEffect(estado.encontrado, tiempoRestante) {
        if (estado.encontrado || tiempoRestante <= 0) {
            game.calcularResultados(tiempoRestante, direccion)
            onTerminar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(estado.color)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("⏱ 00:${tiempoRestante.toString().padStart(2, '0')}", style = MaterialTheme.typography.titleLarge)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Dirección: $direccion°", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(estado.mensaje, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        }

        Text("Gira el teléfono para buscar", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PantallaResultado(
    game: GameEngine,
    mejorTiempo: Int,
    onReiniciar: () -> Unit,
    onVolverMenu: () -> Unit
) {
    val exito = game.precision > 0

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (exito) "¡LO ENCONTRASTE!" else "¡TIEMPO AGOTADO!",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tiempo empleado: ${game.tiempoEmpleado}s")
                Text("Precisión: ${game.precision}%")
                Text("Puntuación final: ${game.puntaje} pts", style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onReiniciar, modifier = Modifier.fillMaxWidth()) {
            Text("JUGAR DE NUEVO")
        }
        OutlinedButton(onClick = onVolverMenu, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("VOLVER AL MENÚ")
        }
    }
}