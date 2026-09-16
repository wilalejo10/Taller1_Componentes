package com.example.taller1_componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.taller1_componentes.game.GameEngine
import com.example.taller1_componentes.sensors.OrientationSensor
import com.example.taller1_componentes.ui.pantallas.PantallaComoJugar
import com.example.taller1_componentes.ui.pantallas.PantallaDashboard
import com.example.taller1_componentes.ui.pantallas.PantallaJuego
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

enum class Pantalla {
    DASHBOARD,
    JUEGO,
    COMO_JUGAR
}

@Composable
fun EscondidasApp() {

    val context = LocalContext.current

    val sensor = remember { OrientationSensor(context = context) }
    val game = remember { GameEngine() }

    var pantalla by remember { mutableStateOf(Pantalla.DASHBOARD) }

    // Estos valores solo duran mientras la app esta abierta
    var mejorPuntaje by remember { mutableIntStateOf(0) }
    var mejorTiempo by remember { mutableIntStateOf(0) }

    when (pantalla) {

        Pantalla.DASHBOARD -> PantallaDashboard(
            mejorTiempo = mejorTiempo,
            mejorPuntaje = mejorPuntaje,
            onNuevaPartida = {
                game.startGame()
                pantalla = Pantalla.JUEGO
            },
            onComoJugar = { pantalla = Pantalla.COMO_JUGAR }
        )

        Pantalla.JUEGO -> PantallaJuego(
            sensor = sensor,
            game = game,
            mostrarPistas = true,
            onFinalizarPartida = { puntaje, tiempo, gano ->
                if (puntaje > mejorPuntaje) mejorPuntaje = puntaje
                if (gano && (mejorTiempo == 0 || tiempo < mejorTiempo)) mejorTiempo = tiempo
            },
            onVolver = { pantalla = Pantalla.DASHBOARD }
        )

        Pantalla.COMO_JUGAR -> PantallaComoJugar(
            onVolver = { pantalla = Pantalla.DASHBOARD }
        )
    }
}