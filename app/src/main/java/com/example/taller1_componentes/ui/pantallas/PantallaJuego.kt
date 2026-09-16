package com.example.taller1_componentes.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taller1_componentes.game.GameEngine
import com.example.taller1_componentes.game.Proximidad
import com.example.taller1_componentes.sensors.OrientationSensor
import com.example.taller1_componentes.ui.componentes.Brujula
import com.example.taller1_componentes.ui.componentes.Conejo
import com.example.taller1_componentes.ui.componentes.ExpresionConejo
import com.example.taller1_componentes.ui.componentes.FondoBosque
import com.example.taller1_componentes.ui.componentes.TarjetaJuego
import com.example.taller1_componentes.ui.componentes.puntoCardinal
import com.example.taller1_componentes.ui.theme.ColoresJuego
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun PantallaJuego(
    sensor: OrientationSensor,
    game: GameEngine,
    mostrarPistas: Boolean,
    onFinalizarPartida: (puntaje: Int, tiempo: Int, gano: Boolean) -> Unit,
    onVolver: () -> Unit
) {

    var direccion by remember { mutableStateOf(0) }
    var tiempo by remember { mutableStateOf(0) }
    var tiempoRestante by remember { mutableStateOf(game.tiempoTotal) }

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

    LaunchedEffect(juegoFinalizado) {
        while (!juegoFinalizado) {
            direccion = sensor.direction
            delay(100)
        }
    }

    LaunchedEffect(juegoFinalizado) {
        while (!juegoFinalizado) {
            delay(1000)
            tiempo++
        }
    }

    LaunchedEffect(juegoFinalizado) {
        while (!juegoFinalizado) {
            delay(1000)
            tiempoRestante--
        }
    }

    LaunchedEffect(juegoFinalizado) {
        if (juegoFinalizado) {
            game.calcularResultados(tiempoRestante = tiempoRestante, direccionActual = direccion)
            onFinalizarPartida(game.puntaje, game.tiempoEmpleado, estado.encontrado)
        }
    }

    var diferencia = abs(direccion - game.targetDirection)
    if (diferencia > 180) diferencia = 360 - diferencia

    val expresion = when {
        estado.encontrado -> ExpresionConejo.CELEBRANDO
        diferencia <= 30 -> ExpresionConejo.ASOMBRADO
        diferencia <= 70 -> ExpresionConejo.BUSCANDO
        else -> ExpresionConejo.TRISTE
    }

    FondoBosque {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(onClick = onVolver) {
                    Text("Volver")
                }

                Text(
                    text = "Puntos: ${tiempoRestante * 15}",
                    fontSize = 14.sp
                )
            }

            Text(
                text = "Gira y mueve tu telefono para encontrar al personaje escondido.",
                fontSize = 12.sp,
                color = ColoresJuego.TextoSuave,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "Juego iniciado",
                fontSize = 12.sp,
                color = ColoresJuego.TextoSuave,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "Tiempo: ${tiempo}s",
                fontSize = 12.sp,
                color = ColoresJuego.TextoSuave
            )
            Text(
                text = "Tiempo restante: ${tiempoRestante}s",
                fontSize = 12.sp,
                color = ColoresJuego.TextoSuave
            )
            Text(
                text = "Direccion actual: $direccion°",
                fontSize = 12.sp,
                color = ColoresJuego.TextoSuave
            )
            Text(
                text = "Direccion escondida: ${game.targetDirection}°",
                fontSize = 12.sp,
                color = ColoresJuego.TextoSuave
            )

            Spacer(modifier = Modifier.height(18.dp))

            Brujula(
                direccionActual = direccion,
                direccionObjetivo = game.targetDirection,
                diferencia = diferencia,
                expresion = expresion,
                colorEstado = estado.color,
                mensaje = estado.mensaje
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (mostrarPistas && !juegoFinalizado) {
                Text(
                    text = "Pista: esta hacia el " + puntoCardinal(game.targetDirection),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColoresJuego.Frio
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Conejo(
                expresion = expresion,
                tamano = 120.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (juegoFinalizado) {
                TarjetaJuego(modifier = Modifier.fillMaxWidth()) {

                    Conejo(
                        expresion = if (estado.encontrado) ExpresionConejo.CELEBRANDO
                        else ExpresionConejo.TRISTE,
                        tamano = 110.dp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = if (estado.encontrado) "¡Juego terminado! Encontraste la direccion"
                        else "Se acabo el tiempo, no lo encontraste",
                        fontSize = 13.sp,
                        color = ColoresJuego.TextoSuave,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = if (estado.encontrado) "Lo encontraste!" else "Se acabo el tiempo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (estado.encontrado) ColoresJuego.PastoOscuro else ColoresJuego.Caliente,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    )

                    Text(
                        text = "Tiempo que te tardaste: ${game.tiempoEmpleado}s",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Precision: ${game.precision}%",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Puntaje: ${game.puntaje}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            game.reiniciarJuego()
                            tiempo = 0
                            tiempoRestante = game.tiempoTotal
                            direccion = sensor.direction
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColoresJuego.PastoOscuro,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                    ) {
                        Text("Jugar de nuevo")
                    }

                    Button(
                        onClick = onVolver,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Volver al menu")
                    }
                }
            }
        }
    }
}