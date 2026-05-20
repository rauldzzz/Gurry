package com.example.gurry.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gurry.ui.theme.GurryTheme

@Composable
fun HomeTopBar(cups: Int?, level: Int?, experience: Int?, portrait: String?, balance: Float?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Separa izquierda y derecha al máximo
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- IZQUIERDA: Copas ganadas ---
        CupsIndicator(cups = cups, portait = portrait, balance = balance)

        // --- DERECHA: Nivel y Experiencia ---
        ExperienceIndicator(level = level, experience = experience)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    GurryTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            // Caso 1: Usuario Nuevo (0 copas, Lvl 1, 0 exp)
            HomeTopBar(cups = 0, level = 1, experience = 0, "profile_1", 10.25f)

            // Caso 2: Usuario Medio (5 copas, Lvl 12, 55 exp -> 5 barritas y media)
            HomeTopBar(cups = 5, level = 12, experience = 55, "profile_2", 20.25f)

            // Caso 3: Usuario Pro (99 copas, Lvl 50, 90 exp -> 9 barritas)
            HomeTopBar(cups = 99, level = 50, experience = 90, "profile_3", 30.25f)
        }
        }
    }
