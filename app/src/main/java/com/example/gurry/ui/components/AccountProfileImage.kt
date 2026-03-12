package com.example.gurry.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gurry.R
import com.example.gurry.database.UserEntity
import com.example.gurry.ui.theme.GurryTheme

@Composable
fun ProfilePic(userEntity: UserEntity?){
    if (userEntity != null) {
        val picture = getDrawableResource(userEntity.profilePic)
        Image(
            painter = painterResource(picture),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop,
        )
    } else {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun ProfilePicPreview(){
    val fakeUser = UserEntity(
        uid = "123",
        username = "Gurry Tester",
        email = "test@gurry.com",
        balance = 500.0f,
        profilePic = "", // URL vacía o pon una imagen de tus recursos
        isRegistered = true
    )
    GurryTheme {
        ProfilePic(fakeUser)
    }
}
