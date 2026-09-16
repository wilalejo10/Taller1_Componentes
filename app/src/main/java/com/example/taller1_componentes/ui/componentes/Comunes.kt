package com.example.taller1_componentes.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taller1_componentes.ui.theme.ColoresJuego

@Composable
fun TarjetaJuego(
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColoresJuego.Tarjeta)
            .padding(16.dp),
        content = contenido
    )
}

@Composable
fun BotonMenu(
    texto: String,
    icono: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorFondo: Color = ColoresJuego.Tarjeta,
    colorTexto: Color = ColoresJuego.TextoOscuro
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorFondo,
            contentColor = colorTexto
        )
    ) {
        Text(text = "$icono  $texto")
    }
}

@Composable
fun DatoRapido(
    icono: String,
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "$icono $etiqueta", fontSize = 12.sp, color = ColoresJuego.TextoSuave)
        Text(text = valor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
