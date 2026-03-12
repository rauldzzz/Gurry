package com.example.gurry.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerHeigh_XL(){
    Spacer(
        modifier = Modifier.height(50.dp)
    )
}

@Composable
fun SpacerHeigh_L(){
    Spacer(
        modifier = Modifier.height(30.dp)
    )
}

@Composable
fun SpacerHeigh_M(){
    Spacer(
        modifier = Modifier.height(15.dp)
    )
}

@Composable
fun SpacerHeigh_S(){
    Spacer(
        modifier = Modifier.height(10.dp)
    )
}

@Composable
fun SpacerWidth_XL(){
    Spacer(modifier = Modifier.width(50.dp))
}

@Composable
fun SpacerWidth_L(){
    Spacer(modifier = Modifier.width(30.dp))
}

@Composable
fun SpacerWidth_M(){
    Spacer(modifier = Modifier.width(15.dp))
}

@Composable
fun SpacerWidth_S(){
    Spacer(modifier = Modifier.width(10.dp))
}

@Composable
fun SpacerWidth_XS(){
    Spacer(modifier = Modifier.width(5.dp))
}