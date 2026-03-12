package com.example.gurry.ui.screens

import AuthState
import AuthViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.gurry.MainActivity
import com.example.gurry.ui.components.LoginGoogleButton
import com.example.gurry.ui.components.Logo
import com.example.gurry.ui.components.SpacerHeigh_M
import com.example.gurry.ui.components.SpacerHeigh_S
import com.example.gurry.ui.components.SpacerHeigh_XL
import com.example.gurry.ui.theme.GurryTheme
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    innerPadding: PaddingValues,
    authViewModel: AuthViewModel,
){
    val state by authViewModel.authState
    val isRegistered by authViewModel.isRegistered
    val registrationData by authViewModel.registrationState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val handleSignIn = {
        val credentialManager = CredentialManager.create(context)
        val request = authViewModel.prepareGetCredentialRequest()

        scope.launch {
            try {
                // Esto NECESITA ejecutarse desde la UI para mostrar el diálogo de Google
                val result = credentialManager.getCredential(context, request)
                val googleIdCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

                // En cuanto tenemos el token, se lo "devolvemos" al ViewModel
                authViewModel.onTokenReceived(googleIdCredential.idToken)
            } catch (e: Exception) {
                Log.e("Auth", "Error: ${e.message}")
            }
        }
    }

    LaunchedEffect(isRegistered) {
        if (isRegistered == true) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
    if (isRegistered == false && state is AuthState.Success) {
        // Si ya se logueó pero no está registrado, forzamos la pantalla de registro
        RegisterScreen(
            registrationData = registrationData,
            onAction = { action -> authViewModel.onRegisterAction(action) }
        )
    }
    else {
        GurryTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SpacerHeigh_XL()
                    Logo()
                    SpacerHeigh_S()
                    Text(
                        "Welcome to Gurry",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    SpacerHeigh_M()
                    when (state) {
                        is AuthState.Loading -> CircularProgressIndicator()
                        else -> {
                            LoginGoogleButton(onClick = {
                                handleSignIn()
                            })
                            if (state is AuthState.Error) {
                                Log.e("LoginScreen", "Error state")
                            }
                        }
                    }
                }
            }
        }
    }
}

/*
@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(
        innerPadding = PaddingValues(),
        authViewModel = AuthViewModel(),
    )
}
 */