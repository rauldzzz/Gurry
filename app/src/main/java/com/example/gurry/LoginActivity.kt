package com.example.gurry

import com.example.gurry.viewmodels.AuthViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var authViewModel: AuthViewModel
    private val requestZkProofLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val proofBytes = data?.getByteArrayExtra("zk_proof")
            val transcriptBytes = data?.getByteArrayExtra("zk_transcript")

            if (proofBytes != null && transcriptBytes != null) {
                // Pasamos los datos al ViewModel para que ejecute el C++
                authViewModel.verificarPrueba(proofBytes, transcriptBytes, cacheDir.absolutePath)
            } else {
                Log.e("GurryVerifier", "Faltan arrays en la respuesta de la Wallet")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = GurryDatabase.getDatabase(this)
        val dao = db.getUserEntityDao()

        val viewModel: AuthViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(dao) as T
                }
            }
        }

        authViewModel = viewModel
        enableEdgeToEdge()
        setContent {
            GurryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        innerPadding = innerPadding,
                        authViewModel = authViewModel,
                        onLaunchWallet = { lanzarGurryWallet() }
                    )
                }
            }
        }
    }
    private fun lanzarGurryWallet() {
        val intent = Intent("android.intent.action.REQUEST_PROOF")
        if (intent.resolveActivity(packageManager) != null) {
            requestZkProofLauncher.launch(intent)
        } else {
            Log.e("GurryVerifier", "GurryWallet no está instalada.")
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