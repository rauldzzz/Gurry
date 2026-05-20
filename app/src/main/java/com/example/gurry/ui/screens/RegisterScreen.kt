package com.example.gurry.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gurry.R
import com.example.gurry.ui.components.ImageCard
import com.example.gurry.ui.components.SpacerHeigh_M
import com.example.gurry.ui.components.SpacerHeigh_S
import com.example.gurry.ui.components.SpacerHeigh_XL
import com.example.gurry.ui.components.UsernameRegisterInput
import com.example.gurry.ui.theme.GurryTheme
import com.example.gurry.viewmodels.AuthViewModel
import com.example.gurry.viewmodels.RegistrationData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    registrationData: RegistrationData,
    onAction: (AuthViewModel.RegisterAction) -> Unit,
    onLaunchWallet: () -> Unit
){
    val images = listOf("img_1", "img_2", "img_3", "img_4", "img_5")
    GurryTheme{
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SpacerHeigh_XL()
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Logo",
                )
                SpacerHeigh_M()
                UsernameRegisterInput(registrationData, onAction)
                SpacerHeigh_M()
                Text("Select your profile picture:",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,)
                SpacerHeigh_S()
                LazyRow {
                    items(images) { imgName ->
                        ImageCard(
                            isSelected = registrationData.profilePicUrl == imgName,
                            onClick = { onAction(AuthViewModel.RegisterAction.PictureSelected(imgName)) },
                            imgName = imgName
                        )
                    }
                }
                SpacerHeigh_M()
                Button(
                    onClick = {
                        onLaunchWallet()
                    },
                    enabled = !registrationData.isAgeVerified,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(
                            width = 285.dp,
                            height = 40.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ){
                    if(!registrationData.isAgeVerified){
                        Text(
                            text = "Check Age",
                            fontWeight = FontWeight.Bold,
                        )
                    } else{
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Logo",
                        )
                    }

                }


                SpacerHeigh_M()
                Button(
                    onClick = {
                        onAction(AuthViewModel.RegisterAction.SubmitRegistration)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(
                            width = 285.dp,
                            height = 40.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )

                ) {
                    Text("Continue",
                        fontWeight = FontWeight.Bold,
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    val registrationData = RegistrationData(
        username = "username",
        isAgeVerified = false
    )
    RegisterScreen(
        registrationData,
        {},
        {}
    )
}