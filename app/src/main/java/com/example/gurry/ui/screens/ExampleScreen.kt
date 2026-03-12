package com.example.gurry.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gurry.ui.theme.GurryTheme
import com.example.gurry.R


@Composable
fun ExampleScreen(){
    GurryTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.padding(paddingValues = innerPadding)
            ) {
                Text(text = "Hello World")
                Button(onClick = { Log.i("Button", "clicked")}) {
                    Text("Click Me!")
                }
                Text("Upadate")
                Row(modifier = Modifier.wrapContentSize()){
                    Box(modifier = Modifier.size(size = 50.dp).background(color = Color.Black)){
                        Text(text = "Box", color = Color.Red, modifier = Modifier.border(width = 2.dp, color = Color.White))
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(modifier= Modifier.size(50.dp).background(Color.Red)) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Red)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Image test",
                    modifier = Modifier.size(100.dp).background(Color.Green)
                )
            }
        }
    }
}