package com.example.gurry.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun BalanceText(balance: Float?){
    Text(
        text= "Balance: $ ${balance ?: 0.0f}",
        fontSize = 15.sp )
}