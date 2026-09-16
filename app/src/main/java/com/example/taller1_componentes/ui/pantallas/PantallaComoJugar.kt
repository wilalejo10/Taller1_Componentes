package com.example.taller1_componentes.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taller1_componentes.ui.componentes.FondoBosque

@Composable
fun PantallaComoJugar(onVolver: () -> Unit) {

FondoBosque {

    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
        text = "Como jugar",
        style = MaterialTheme.typography.headlineMedium
        )

        Text(
        text = "1. El personaje se esconde en una direccion al azar.",
        modifier = Modifier.padding(top = 16.dp)
        )

        Text(
        text = "2. Gira tu telefono para buscar esa direccion.",
        modifier = Modifier.padding(top = 8.dp)
        )

        Text(
        text = "3. La app te dice si estas Frio, Tibio o Caliente.",
        modifier = Modifier.padding(top = 8.dp)
        )

        Text(
        text = "4. Acercate lo suficiente antes de que se acabe el tiempo.",
        modifier = Modifier.padding(top = 8.dp)
        )

        Button(
        onClick = onVolver,
        modifier = Modifier.padding(top = 32.dp)
        ) {
        Text("VOLVER")
        }
    }
}
}