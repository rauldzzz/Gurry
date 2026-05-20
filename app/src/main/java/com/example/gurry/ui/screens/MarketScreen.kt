package com.example.gurry.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.gurry.R
import com.example.gurry.database.HorseEntity
import com.example.gurry.database.UserEntity
import com.example.gurry.ui.components.GurryTabBar
import com.example.gurry.ui.components.MarketHorseLazyList
import com.example.gurry.ui.components.SpacerHeigh_L
import com.example.gurry.ui.components.SpacerHeigh_M
import com.example.gurry.ui.components.SpacerHeigh_S
import com.example.gurry.ui.components.SpacerWidth_S
import com.example.gurry.ui.theme.GurryTheme
import com.example.gurry.ui.viewmodels.MarketViewModel
import com.example.gurry.viewmodels.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MarketScreen(marketViewModel: MarketViewModel, authViewModel: AuthViewModel) {
    val userEntity by authViewModel.currentUserData.collectAsState(initial = null)
    val horsesEntity by marketViewModel.marketHorses.collectAsState(initial = null)
    val context = LocalContext.current
    MarketContent(
        userEntity = userEntity,
        marketHorses = horsesEntity,
        onBuyClick = { horse ->
            if (userEntity != null && userEntity!!.uid.isNotEmpty()) {
                marketViewModel.buyHorse(
                    horse = horse, // Usamos el caballo que viene del click
                    user = userEntity!!,
                    onSuccess = {
                        Toast.makeText(context, "Horse buy successful!", Toast.LENGTH_SHORT).show()
                    },
                    onError = { errorMsg ->
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                )
            } else {
                Toast.makeText(context, "Charging ...", Toast.LENGTH_SHORT).show()
            }
        }
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MarketContent(marketHorses: List<HorseEntity>?, userEntity: UserEntity?, onBuyClick: (HorseEntity) -> Unit ) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Market", "My Ops.")
    GurryTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SpacerHeigh_L()
                Row(
                ) {
                    Text("Horse Market",
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
                GurryTabBar(selectedTabIndex, tabs, { newIndex ->
                    selectedTabIndex = newIndex})
                SpacerHeigh_S()

                when (selectedTabIndex) {
                    0 -> MarketHorseLazyList(
                        marketHorses,
                        onBuyClick
                    )
                    1 -> Text("Pantalla 2")  // Pantalla 2
                }

            }
        }
    }
}

@Preview
@Composable
fun MarketScreenPreview(){
    val fakeMarketHorse = listOf(
        HorseEntity(
            metadata_id = "h1",
            name = "Thunder Bolt",
            color = "Black",
            price = 45.50f,
            score = 88.5f,
            display_3d = "",
            horse_img = ""
        ),
        HorseEntity(
            metadata_id = "h2",
            name = "Solar Flare",
            color = "White",
            price = 12.0f,
            score = 65.0f,
            display_3d = "",
            horse_img = ""
        ),
        HorseEntity(
            metadata_id = "h3",
            name = "Crypto King",
            color = "Chestnut",
            price = 30.0f,
            score = 79.2f,
            display_3d = "",
            horse_img = ""
        )
    )
    val fakeUser = UserEntity(
        uid = "123",
        username = "Gurry Tester",
        email = "test@gurry.com",
        balance = 500.0f,
        profilePic = "",
        isRegistered = true
    )
    GurryTheme {
        MarketContent(
            fakeMarketHorse, fakeUser,
            onBuyClick = {},
        )
    }
}