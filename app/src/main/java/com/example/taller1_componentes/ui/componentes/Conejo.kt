package com.example.taller1_componentes.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taller1_componentes.R


enum class ExpresionConejo(val recurso: Int, val descripcion: String) {
    BUSCANDO(R.drawable.conejo_buscando, "Conejo buscando"),
    ASOMBRADO(R.drawable.conejo_asombrado, "Conejo asombrado"),
    FELIZ(R.drawable.conejo_feliz, "Conejo feliz"),
    TRISTE(R.drawable.conejo_triste, "Conejo triste"),
    CELEBRANDO(R.drawable.conejo_celebrando, "Conejo celebrando")
}

@Composable
fun Conejo(
    expresion: ExpresionConejo,
    modifier: Modifier = Modifier,
    tamano: Dp = 120.dp
) {
    Image(
        painter = painterResource(id = expresion.recurso),
        contentDescription = expresion.descripcion,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(tamano)
    )
}
