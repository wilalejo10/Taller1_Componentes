package com.example.taller1_componentes.game

import androidx.compose.ui.graphics.Color

data class EstadoProximidad(
    val mensaje: String,
    val color: Color,
    val encontrado: Boolean
)

object Proximidad {

    fun calcular(direccionActual: Int, direccionObjetivo: Int): EstadoProximidad {

        var diferencia = kotlin.math.abs(direccionActual - direccionObjetivo)

        if (diferencia > 180) {
            diferencia = 360 - diferencia
        }

        return when {
            diferencia <= 10 -> EstadoProximidad(
                mensaje = "¡FELCICITACIONES LO ENCONTRASTE!",
                color = Color(0xFF85EE0D),
                encontrado = true
            )

            diferencia <= 30 -> EstadoProximidad(
                mensaje = "Muy caliente",
                color = Color.Red,
                encontrado = false
            )

            diferencia <= 70 -> EstadoProximidad(
                mensaje = "Tibio, ya casi",
                color = Color(0xFFFFC107),
                encontrado = false
            )

            else -> EstadoProximidad(
                mensaje = "Que Frío",
                color = Color.Blue,
                encontrado = false
            )
        }
    }
}