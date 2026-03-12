package com.example.gurry.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // <--- ESTE IMPORT ES CRUCIAL
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gurry.R // <--- ASEGÚRATE DE QUE ESTE IMPORT ESTÉ
import com.example.gurry.database.HorseEntity
import com.example.gurry.ui.components.* import com.example.gurry.ui.viewmodels.StableViewModel

@Composable
fun StableScreen(stableViewModel: StableViewModel) {
    val myHorses by stableViewModel.myHorses.collectAsState()
    var selectedHorse by remember { mutableStateOf<HorseEntity?>(null) }

    LaunchedEffect(myHorses) {
        if (selectedHorse == null && myHorses.isNotEmpty()) {
            selectedHorse = myHorses.first()
        }
    }

    StableContent(
        horses = myHorses,
        selectedHorse = selectedHorse,
        onSelectHorse = { horse -> selectedHorse = horse }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StableContent(
    horses: List<HorseEntity>,
    selectedHorse: HorseEntity?,
    onSelectHorse: (HorseEntity) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpacerHeigh_L()
            Row(
            ) {
                Text("My Horses",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold)
                SpacerWidth_S()
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.horse),
                    contentDescription = "horse icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            SpacerHeigh_M()

            if (selectedHorse == null) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No horses owned yet.", color = Color.Gray)
                }
            } else {
                // --- ZONA SUPERIOR (3D y Stats) ---
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Visor 3D
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.3f))
                    ) {
                        // Asegúrate de que Horse3DViewer está importado o en el mismo paquete
                        Horse3DViewer(
                            modelUrl = "horse.glb",
                        )
                    }

                    SpacerWidth_S()

                    // Stats
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = selectedHorse.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "${selectedHorse.score}",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        SpacerHeigh_M()
                        HorizontalDivider() // "Divider()" ahora es "HorizontalDivider()" en Material3 reciente
                        SpacerHeigh_M()

                        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxHeight()) {
                            StatBarDisplay("Gallop", selectedHorse.gallop)
                            StatBarDisplay("Snatch", selectedHorse.snatch)
                            StatBarDisplay("Endurance", selectedHorse.endurance)
                            StatBarDisplay("Accel.", selectedHorse.acceleration)
                        }
                    }
                }
            }

            SpacerHeigh_L()

            Text(
                text = "Select Horse",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            SpacerHeigh_S()

            // --- LISTA INFERIOR ---
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(80.dp)
            ) {
                // Aquí es donde fallaba antes sin el import de .lazy.items
                items(horses) { horse ->
                    StableListItem(
                        horse = horse,
                        isSelected = horse.metadata_id == selectedHorse?.metadata_id,
                        onClick = { onSelectHorse(horse) }
                    )
                }
            }
            SpacerHeigh_S()
        }
    }
}

// --- COMPONENTES AUXILIARES (Pégalos al final del archivo) ---

@Composable
fun StatBarDisplay(label: String, value: Float) {
    val colorLow = MaterialTheme.colorScheme.onSurfaceVariant
    val colorHigh = MaterialTheme.colorScheme.secondary
    val fraction = (value / 100f).coerceIn(0f, 1f)
    val barColor = lerp(colorLow, colorHigh, fraction)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            Text(text = "${value.toInt()}", style = MaterialTheme.typography.bodySmall, color = barColor)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun StableListItem(
    horse: HorseEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val elevation = if (isSelected) 8.dp else 2.dp

    Card(
        modifier = Modifier
            .width(180.dp)
            .fillMaxHeight()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(elevation),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Horse Image",
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = horse.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "Score: ${horse.score}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
