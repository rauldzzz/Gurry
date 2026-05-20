package com.example.gurry.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gurry.database.UserEntity
import com.example.gurry.ui.components.BalanceText
import com.example.gurry.ui.components.GurryTabBar
import com.example.gurry.ui.components.ProfilePic
import com.example.gurry.ui.components.SpacerHeigh_M
import com.example.gurry.ui.components.SpacerHeigh_S
import com.example.gurry.ui.components.SpacerHeigh_XL
import com.example.gurry.ui.components.usernameText
import com.example.gurry.ui.theme.GurryTheme
import com.example.gurry.viewmodels.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(authViewModel: AuthViewModel) {
    val userEntity by authViewModel.currentUserData.collectAsState(initial = null)

    AccountContent(
        userEntity = userEntity,
        onSignOut = { authViewModel.signOut() }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountContent(
    userEntity: UserEntity?,
    onSignOut: () -> Unit
) {
    // 1. Estado para saber en qué pestaña estamos (0 = Datos, 1 = Ajustes)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    // Definimos los títulos e iconos de las pestañas
    val tabs = listOf("Record", "Notifications")
    GurryTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpacerHeigh_XL()
                ProfilePic(userEntity)
                SpacerHeigh_S()
                usernameText(userEntity)
                SpacerHeigh_S()
                BalanceText(userEntity?.balance)
                SpacerHeigh_M()
                GurryTabBar(selectedTabIndex, tabs, { newIndex ->
                    selectedTabIndex = newIndex} )
                when (selectedTabIndex) {
                    0 -> Text("") // Pantalla 1
                    1 -> Text("")  // Pantalla 2
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun AccountScreenPreview(){
    val fakeUser = UserEntity(
        uid = "123",
        username = "Gurry Tester",
        email = "test@gurry.com",
        balance = 500.0f,
        profilePic = "", // URL vacía o pon una imagen de tus recursos
        isRegistered = true
    )
    GurryTheme {
        AccountContent(
            userEntity = fakeUser,
            onSignOut = {},
        )

    }
}