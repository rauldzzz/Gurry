package com.example.gurry.ui.components

import AuthViewModel
import RegistrationData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gurry.R


@Composable
fun UsernameRegisterInput(
    registrationData: RegistrationData,
    onAction: (AuthViewModel.RegisterAction) -> Unit
){
    OutlinedTextField(
        value = registrationData.username,
        onValueChange = { input -> onAction(AuthViewModel.RegisterAction.NameChanged(input)) },
        label = { Text("Username") },
        modifier = Modifier
            .size(
                height = 65.dp,
                width = 285.dp
            ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun getDrawableResource(name: String): Int {
    return when (name) {
        "img_1" -> R.drawable.profile_1
        "img_2" -> R.drawable.profile_2
        "img_3" -> R.drawable.profile_3
        "img_4" -> R.drawable.profile_4
        "img_5" -> R.drawable.profile_5
        else -> R.drawable.profile_1 // Imagen por defecto
    }
}

@Composable
fun ImageCard(
    imgName: String, // El ID de tu drawable
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val resId = getDrawableResource(imgName)
    Surface(
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        // Resaltamos si coincide con el estado del ViewModel
        border = if (isSelected) BorderStroke(3.dp, MaterialTheme.colorScheme.primary) else null,
        tonalElevation = if (isSelected) 8.dp else 0.dp
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = "Profile Option",
            modifier = Modifier.fillMaxSize()
        )
    }
}

