package com.example.taller1_componentes.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class GameEngine {

    // Cambiado a mutableStateOf para que Compose escuche sus cambios
    var targetDirection: Int by mutableStateOf(0)
        private set

    var tiempoTotal: Int = 60

    var tiempoEmpleado: Int by mutableStateOf(0)
        private set

    var precision: Int by mutableStateOf(0)
        private set

    var puntaje: Int by mutableStateOf(0)
        private set

    fun startGame() {
        targetDirection = Random.nextInt(0, 360)
    }

    fun calcularResultados(tiempoRestante: Int, direccionActual: Int) {
        tiempoEmpleado = tiempoTotal - tiempoRestante

        var diferencia = abs(direccionActual - targetDirection)
        if (diferencia > 180) {
            diferencia = 360 - diferencia
        }

        precision = max(0, 100 - (diferencia * 10))

        val puntosPorTiempo = tiempoRestante * 15
        val puntosPorPrecision = precision * 5
        puntaje = max(0, puntosPorTiempo + puntosPorPrecision)
    }

    fun reiniciarJuego() {
        startGame()
    }
}