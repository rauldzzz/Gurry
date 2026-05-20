package com.example.gurry.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.gurry.database.UserEntity
import com.example.gurry.ui.theme.GurryTheme

@Composable
fun usernameText(userEntity: UserEntity?){
    Text(
        text = userEntity?.username ?: "Cargando...",
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun usernameTextPreview(){
    val fakeUser = UserEntity(
        uid = "123",
        username = "Gurry Tester",
        email = "test@gurry.com",
        balance = 500.0f,
        profilePic = "", // URL vacía o pon una imagen de tus recursos
        isRegistered = true
    )
    GurryTheme {
        usernameText(fakeUser)
    }
}