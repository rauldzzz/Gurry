package com.example.gurry.ui.screens

import AuthViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gurry.R
import com.example.gurry.database.UserEntity
import com.example.gurry.ui.components.HomeTopBar
import com.example.gurry.ui.components.SpacerHeigh_M
import com.example.gurry.ui.components.SpacerHeigh_S
import com.example.gurry.ui.components.SpacerWidth_S
import com.example.gurry.ui.theme.GurryTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(authViewModel: AuthViewModel) {
    // Observamos al usuario en tiempo real
    val userEntity by authViewModel.currentUserData.collectAsState(initial = null)
    HomeContent(
        userEntity = userEntity,
        onRaceClick = { /* TODO: Navegar a pantalla de selección de carrera */ },
        onBuyMoreClick = { /* TODO: Navegar a tienda */ }
    )
}

@Composable
fun HomeContent(
    userEntity: UserEntity?,
    onRaceClick: () -> Unit,
    onBuyMoreClick: () -> Unit
) {
    // Valores por defecto si aún está cargando
    val cups = userEntity?.cups
    val level = userEntity?.level
    val experience = userEntity?.experience
    val portrait = userEntity?.profilePic
    val balance = userEntity?.balance

    GurryTheme {
        Scaffold(
            topBar = {
                HomeTopBar(cups = cups, level = level, experience = experience, portrait = portrait, balance = balance)
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = 0.dp
                    )
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- CARD 1: RACE ---
                HomeActionCard(
                    modifier = Modifier.weight(1f),
                    onClick = onRaceClick,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Columna Izquierda: Logo del caballo
                        Image(
                            painter = painterResource(R.drawable.logo), // Usa el nombre de tu asset
                            contentDescription = "Horse",
                            modifier = Modifier.size(145.dp)
                        )

                        // Columna Derecha: Texto RACE y Banderas
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "RACE",
                                fontSize = 70.sp,
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.flag),
                                    contentDescription = "Flag 1",
                                    modifier = Modifier.size(75.dp)
                                )
                                Image(
                                    painter = painterResource(R.drawable.flag),
                                    contentDescription = "Flag 2",
                                    modifier = Modifier.size(75.dp)
                                )
                            }
                        }
                    }
                }
                SpacerHeigh_S()
                // --- CARD 2: DAILY REWARD ---
                DailyRewardCard(modifier = Modifier.weight(1f))
                SpacerHeigh_S()
                // --- CARD 3: BUY MORE ---
                HomeActionCard(
                    modifier = Modifier.weight(1f),
                    onClick = onBuyMoreClick,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Columna Izquierda: 4 Monedas en cuadrícula (2x2)
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Image(painter = painterResource(R.drawable.dolar), contentDescription = null, modifier = Modifier.size(65.dp))
                                Image(painter = painterResource(R.drawable.dolar), contentDescription = null, modifier = Modifier.size(65.dp))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Image(painter = painterResource(R.drawable.dolar), contentDescription = null, modifier = Modifier.size(65.dp))
                                Image(painter = painterResource(R.drawable.dolar), contentDescription = null, modifier = Modifier.size(65.dp))
                            }
                        }

                        // Columna Derecha: Texto BUY MORE
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "BUY",
                                fontSize = 62.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFFFFCA28), // Amarillo dorado como el mockup
                                lineHeight = 50.sp
                            )
                            Text(
                                text = "MORE",
                                fontSize = 62.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFFFFCA28),
                                lineHeight = 50.sp
                            )
                        }
                    }
                }
                SpacerHeigh_S()
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun HomeActionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Esquinas redondeadas restauradas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        content()
    }
}

@Composable
fun DailyRewardCard(modifier: Modifier = Modifier) {
    var timeRemainingInMillis by remember { mutableLongStateOf(0L) }
    val isReady = timeRemainingInMillis <= 0

    LaunchedEffect(key1 = timeRemainingInMillis) {
        if (timeRemainingInMillis > 0) {
            delay(1000L)
            timeRemainingInMillis -= 1000L
        }
    }

    HomeActionCard(
        modifier = modifier,
        onClick = {
            if (isReady) timeRemainingInMillis = 24 * 60 * 60 * 1000L
        },
        // Fondo secundario si está OPEN, surface si está bloqueado
        containerColor = if (isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Columna Izquierda: Cofre
            Image(
                painter = painterResource(if (isReady) R.drawable.opened_chesed else R.drawable.closed_chest),
                contentDescription = "Chest",
                modifier = Modifier.size(110.dp)
            )

            // Columna Derecha: Textos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isReady) {
                    Text(
                        text = "OPEN",
                        fontSize = 70.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    val hours = TimeUnit.MILLISECONDS.toHours(timeRemainingInMillis)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemainingInMillis) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemainingInMillis) % 60

                    Text(
                        text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Next reward in...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val fakeUser = UserEntity(
        uid = "123",
        username = "Gurry Tester",
        cups = 5,
        level = 12,
        experience = 55
    )
    HomeContent(
        fakeUser,
        onRaceClick = {},
        onBuyMoreClick = {}
    )
}