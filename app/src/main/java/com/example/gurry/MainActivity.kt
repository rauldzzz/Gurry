package com.example.gurry

import AuthViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gurry.database.GurryDatabase
import com.example.gurry.ui.components.NavBar
import com.example.gurry.ui.theme.GurryTheme
import com.example.gurry.ui.viewmodels.MarketViewModel
import com.example.gurry.ui.viewmodels.StableViewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Inicializamos la DB y el DAO
        val db = GurryDatabase.getDatabase(this)
        val userDao = db.getUserEntityDao()
        val horseDao = db.getHorseDao()

        // 2. Usamos un Factory para pasar el DAO al ViewModel
        val authViewModel: AuthViewModel by viewModels {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(userDao) as T
                }
            }
        }
        val marketViewModel: MarketViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MarketViewModel(horseDao, userDao) as T
                }
            }
        }
        val stableViewModel: StableViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StableViewModel(horseDao) as T
                }
            }
        }
        enableEdgeToEdge()
        setContent {
            GurryTheme {
                NavBar(authViewModel, marketViewModel, stableViewModel)
            }
        }
    }
}