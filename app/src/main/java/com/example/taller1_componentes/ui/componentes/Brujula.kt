package com.example.taller1_componentes.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taller1_componentes.ui.theme.ColoresJuego
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Brujula(
    direccionActual: Int,
    direccionObjetivo: Int,
    diferencia: Int,
    expresion: ExpresionConejo,
    colorEstado: Color,
    mensaje: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 260.dp
) {

val visibilidad = when {
    diferencia <= 10 -> 1f
    diferencia <= 30 -> 0.8f
    diferencia <= 70 -> 0.4f
    else -> 0.15f
}

val radio = tamano / 2

val angObjetivo = Math.toRadians(direccionObjetivo.toDouble())
val dx = (radio.value * 0.55f * sin(angObjetivo)).toInt().dp
val dy = (-radio.value * 0.55f * cos(angObjetivo)).toInt().dp

Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Box(
        modifier = Modifier.size(tamano),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val centro = Offset(size.width / 2f, size.height / 2f)
            val r = size.minDimension / 2f

            drawCircle(
                color = Color.LightGray,
                radius = r,
                center = centro,
                style = Stroke(width = 4f)
            )

            val angulo = Math.toRadians(direccionActual.toDouble())
            val fin = Offset(
                x = centro.x + r * sin(angulo).toFloat(),
                y = centro.y - r * cos(angulo).toFloat()
            )

            drawLine(
                color = colorEstado,
                start = centro,
                end = fin,
                strokeWidth = 8f
            )
        }

        Conejo(
            expresion = expresion,
            tamano = tamano * 0.30f,
            modifier = Modifier
                .offset(x = dx, y = dy)
                .alpha(visibilidad)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = mensaje.uppercase(),
        color = colorEstado,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1
    )
}
}

fun puntoCardinal(grados: Int): String {
val g = ((grados % 360) + 360) % 360
return when {
    g < 23 || g >= 338 -> "NORTE"
    g < 68 -> "NORESTE"
    g < 113 -> "ESTE"
    g < 158 -> "SURESTE"
    g < 203 -> "SUR"
    g < 248 -> "SUROESTE"
    g < 293 -> "OESTE"
    else -> "NOROESTE"
}
}