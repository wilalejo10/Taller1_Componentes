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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taller1_componentes.ui.componentes.BotonMenu
import com.example.taller1_componentes.ui.componentes.Conejo
import com.example.taller1_componentes.ui.componentes.DatoRapido
import com.example.taller1_componentes.ui.componentes.ExpresionConejo
import com.example.taller1_componentes.ui.componentes.FondoBosque
import com.example.taller1_componentes.ui.componentes.TarjetaJuego
import com.example.taller1_componentes.ui.theme.ColoresJuego

@Composable
fun PantallaDashboard(
mejorTiempo: Int,
mejorPuntaje: Int,
onNuevaPartida: () -> Unit,
onComoJugar: () -> Unit
) {
FondoBosque {

    Column(
    modifier = Modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())
    .padding(horizontal = 22.dp, vertical = 28.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
    ) {

    TarjetaJuego(modifier = Modifier.padding(top = 10.dp)) {

        Text(
        text = "Bienvenido a ENCUENTRAME",
        color = ColoresJuego.Caliente,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
        )

        Conejo(
        expresion = ExpresionConejo.BUSCANDO,
        tamano = 145.dp,
        modifier = Modifier.padding(top = 20.dp).fillMaxWidth()
        )

        Text(
        text = "Encuentra al personaje escondido antes de que se acabe el tiempo.",
        color = ColoresJuego.TextoSuave,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)
        )

        Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
        ) {
        DatoRapido(
            icono = "P",
            etiqueta = "Mejor puntaje",
            valor = "$mejorPuntaje pts"
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    BotonMenu(
        texto = "NUEVA PARTIDA",
        icono = ">",
        onClick = onNuevaPartida,
        colorFondo = ColoresJuego.PastoOscuro,
        colorTexto = androidx.compose.ui.graphics.Color.White
    )

    Spacer(modifier = Modifier.height(10.dp))

    BotonMenu(texto = "¿COMO JUGAR?",
    icono = ">",
    onClick = onComoJugar,
    colorFondo = ColoresJuego.PastoOscuro,
    colorTexto = androidx.compose.ui.graphics.Color.White)
    Spacer(modifier = Modifier.height(24.dp))
        }
    }
}