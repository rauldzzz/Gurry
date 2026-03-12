package com.example.gurry.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gurry.R
import com.example.gurry.ui.theme.GurryTheme

@Composable
fun CupsIndicator(cups: Int?, portait: String?, balance: Float?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 6.dp)
    ) {
        val picture = getDrawableResource(portait ?: "profile_1")
        Image(
            painter = painterResource(picture),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop,
        )
        SpacerWidth_XS()
        Icon(
            imageVector = Icons.Default.EmojiEvents, // Icono nativo de copa
            contentDescription = "Copas",
            tint = Color(0xFFFFD700), // Color Amarillo Dorado Fijo
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "$cups",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        SpacerWidth_XS()
        Image(
            painter = painterResource(id = R.drawable.dolar),
            contentDescription = "Coin Icon",
            modifier = Modifier
                .size(26.dp),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "$balance",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
@Preview
fun CupsIndicatorPreview() {
    GurryTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            CupsIndicator(0, "profile_1", 10.25f)
        }
    }
}