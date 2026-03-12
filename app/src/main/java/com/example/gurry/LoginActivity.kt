package com.example.gurry

import AuthViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gurry.database.GurryDatabase
import com.example.gurry.ui.screens.LoginScreen
import com.example.gurry.ui.theme.GurryTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Inicializamos la DB y el DAO
        val db = GurryDatabase.getDatabase(this)
        val dao = db.getUserEntityDao()

        // 2. Usamos un Factory para pasar el DAO al ViewModel
        val authViewModel: AuthViewModel by viewModels {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(dao) as T
                }
            }
        }
        enableEdgeToEdge()
        setContent {
            GurryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        innerPadding = innerPadding,
                        authViewModel = authViewModel,
                    )
                }
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    GurryTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LoginScreen(
                innerPadding = innerPadding,
                authViewModel = AuthViewModel(),
            )
        }
    }
}
 */