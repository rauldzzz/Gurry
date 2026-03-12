package com.example.gurry.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExperienceIndicator(level: Int?, experience: Int?) {
    // Calculamos cuántos bloques pintar (cada 10 exp = 1 bloque)
    // Usamos coerceIn para asegurar que si exp es 0 no falle, y si es >100 no pinte de más
    val safeExperience = experience ?: 0
    val blocksFilled = (safeExperience / 10).coerceIn(0, 10)

    Column(
        horizontalAlignment = Alignment.End // Alineado a la derecha
    ) {
        // Texto de Nivel
        Text(
            text = "Exp - Lvl $level",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Los 10 Rectángulos
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp) // Espacio entre rectángulos
        ) {
            repeat(10) { index ->
                val isFilled = index < blocksFilled

                Box(
                    modifier = Modifier
                        .width(10.dp)  // Ancho del rectángulo
                        .height(14.dp) // Alto del rectángulo
                        .background(
                            color = if (isFilled) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isFilled) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}