package com.example.gurry.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gurry.R
import com.example.gurry.database.HorseEntity
import com.example.gurry.ui.theme.GurryTheme

@Composable
fun MarketHorseLazyList(
    horses: List<HorseEntity>?,
    onBuyClick: (HorseEntity) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(horses ?: emptyList()) { horse ->
            MarketHorseItem(horse, onBuyClick)
        }
    }
}

@Composable
fun MarketHorseItem(
    horse: HorseEntity,
    onBuyClick: (HorseEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // COLUMNA 1: FOTO (Izquierda)
            // Usamos un icono por defecto si la URL está vacía (como en tu fake data)
            if (horse.horse_img.isNotEmpty()) {
                Logo()
            } else {
                // Placeholder si no hay imagen
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Horse Image",
                    modifier = Modifier.size(80.dp)
                )
            }

            SpacerWidth_S()

            // COLUMNA 2: DATOS (Centro - Ocupa el espacio sobrante)
            Column(
                modifier = Modifier.weight(1f) // Esto empuja la columna 3 a la derecha
            ) {
                Text(
                    text = horse.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Color: ${horse.color}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                SpacerHeigh_S()
                Text(
                    text = "Market",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            SpacerWidth_S()

            // COLUMNA 3: PRECIO Y ACCIÓN (Derecha)
            Column(
                horizontalAlignment = Alignment.End // Todo alineado a la derecha
            ) {
                // Score con estrellita
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${horse.score}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Score",
                        tint = Color(0xFFFFD700), // Dorado
                        modifier = Modifier.size(18.dp)
                    )
                }

                SpacerHeigh_S()

                Text(
                    text = "$ ${horse.price}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,

                )

                Button(
                    onClick = { onBuyClick(horse) },
                    modifier = Modifier.size(width = 70.dp, height = 25.dp),
                    contentPadding = PaddingValues(0.dp) // Para que quepa el texto
                ) {
                    Text("Buy", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarketHorseLazyListPreview() {
    // 1. Generamos una lista falsa con variedad de datos
    val fakeHorses = listOf(
        HorseEntity(
            metadata_id = "1",
            name = "Thunder Bolt",
            color = "Black",
            score = 85.5f,
            price = 45.0f,
            horse_img = "" // Vacío para probar el icono de placeholder
        ),
        HorseEntity(
            metadata_id = "2",
            name = "Golden Wind",
            color = "Palomino",
            score = 92.0f,
            price = 120.50f,
            horse_img = ""
        ),
        HorseEntity(
            metadata_id = "3",
            name = "Shadow Runner",
            color = "Gray",
            score = 65.0f,
            price = 15.99f,
            horse_img = ""
        ),
        HorseEntity(
            metadata_id = "4",
            name = "Cosmic Ray",
            color = "White",
            score = 98.2f,
            price = 350.0f,
            horse_img = ""
        )
    )

    GurryTheme {
        // 2. Llamamos a la lista pasando los datos falsos
        MarketHorseLazyList(
            horses = fakeHorses,
            onBuyClick = {} // Lambda vacía porque es solo visual
        )
    }
}