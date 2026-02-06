package com.margaritaolivera.almacenropa.features.inventory.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StockLabel(stock: Int) {
    val (color, text) = when {
        stock == 0 -> Color.Red to "Agotado"
        stock < 5 -> Color(0xFFFFA500) to "Últimas unidades ($stock)" // Naranja
        else -> Color(0xFF2E7D32) to "En Stock ($stock)" // Verde
    }

    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}