package com.example.gurry.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gurry.database.HorseDao
import com.example.gurry.database.HorseEntity
import com.example.gurry.database.UserEntity
import com.example.gurry.database.UserEntityDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.RoundingMode

class MarketViewModel(private val horseDao: HorseDao, private val userDao: UserEntityDao) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _marketHorses = MutableStateFlow<List<HorseEntity>>(emptyList())

    // 2. VARIABLE PÚBLICA (Lo que ve la UI)
    val marketHorses = _marketHorses.asStateFlow()

    // Trabajo de escucha (opcional, pero buena práctica para poder cancelarlo)
    private var marketJob: Job? = null

    init {
        // Al arrancar, empezamos a escuchar la Base de Datos Local
        listenToMarket()
        // Al iniciar, intentamos actualizar los datos desde la nube
        fetchHorsesFromFirebase()
    }

    // Modifica el ViewModel para que use esta lógica antes de guardar
    fun updateBalanceSafe(currentBalance: Float, amountToSubtract: Float): Float {
        return (currentBalance - amountToSubtract).toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
            .toFloat()
    }
    fun buyHorse(
        horse: HorseEntity,
        user: UserEntity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        // A) Comprobamos el saldo (usando 'balance')
        if (user.balance < horse.price) {
            onError("There is no enough money. You have ${user.balance} but you need ${horse.price}")
            return
        }

        val newBalance = updateBalanceSafe(user.balance, horse.price)

        viewModelScope.launch {
            // B) ACTUALIZACIÓN LOCAL (Rápida)
            try {
                // 1. Cambiamos el dueño del caballo
                horseDao.updateOwner(horse.metadata_id, user.uid)
                // 2. Actualizamos el saldo del usuario
                userDao.updateBalance(user.uid, newBalance)

                onSuccess()
            } catch (e: Exception) {
                onError("Error local: ${e.message}")
            }
        }

        // C) ACTUALIZACIÓN EN NUBE (FIREBASE)
        // 1. Actualizar dueño del caballo
        db.collection("crypto_horses")
            .whereEqualTo("metadata_id", horse.metadata_id)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    db.collection("crypto_horses").document(doc.id)
                        .update("id_owner", user.uid)
                }
            }

        // 2. Actualizar saldo del usuario (campo 'balance')
        db.collection("users").document(user.uid)
            .update("balance", newBalance)
            .addOnFailureListener { e ->
                Log.e("GurryMarket", "Error actualizando saldo en nube", e)
            }
    }

    private fun listenToMarket() {
        marketJob?.cancel()
        marketJob = viewModelScope.launch {
            // Nos suscribimos al Flow del DAO
            horseDao.getMarketHorses().collect { listaDeCaballos ->
                // Cada vez que Room avise de cambios, actualizamos nuestra variable
                _marketHorses.value = listaDeCaballos
            }
        }
    }

    fun fetchHorsesFromFirebase() {
        Log.d("GurryMarket", "Descargando caballos de Firestore...")

        db.collection("crypto_horses")
            .whereEqualTo("id_owner", "0") // Solo los que están en venta
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val horsesList = snapshot?.toObjects(HorseEntity::class.java) ?: emptyList()

                viewModelScope.launch {
                    // Esto mantendrá Room sincronizado al segundo con Firebase
                    horseDao.refreshMarket(horsesList)
                }
            }
    }
}