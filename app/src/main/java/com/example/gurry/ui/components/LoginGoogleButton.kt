package com.example.gurry.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.gurry.R

@Composable
fun LoginGoogleButton(onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.android_button),
        contentDescription = "Google Login Button",
        modifier = Modifier.clickable { onClick() }
    )
}

@Preview
@Composable
fun LoginGoogleButtonPreview(){
    LoginGoogleButton(onClick = {})
}