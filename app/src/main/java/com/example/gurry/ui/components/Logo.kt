package com.example.gurry.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.gurry.R

@Composable
fun Logo(){
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo"
    )
}