package com.example.gurry.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gurry.database.HorseDao
import com.example.gurry.database.HorseEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StableViewModel(private val horseDao: HorseDao) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // 1. Estado UI: Lista de MIS caballos
    private val _myHorses = MutableStateFlow<List<HorseEntity>>(emptyList())
    val myHorses = _myHorses.asStateFlow()

    private var stableJob: Job? = null

    init {
        // Al iniciarse, cargamos los datos del usuario actual
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            listenToMyStable(uid)
            fetchMyHorsesFromFirebase(uid)
        }
    }

    // 2. Escuchar la Base de Datos Local (Room)
    // Esto asegura que la UI se actualice instantáneamente al comprar
    private fun listenToMyStable(userId: String) {
        stableJob?.cancel()
        stableJob = viewModelScope.launch {
            horseDao.getMyHorses(userId).collect { horses ->
                _myHorses.value = horses
            }
        }
    }

    // 3. Sincronizar con la Nube (Firebase)
    fun fetchMyHorsesFromFirebase(userId: String) {
        Log.d("GurryStable", "Buscando caballos del usuario: $userId")

        db.collection("crypto_horses")
            .whereEqualTo("id_owner", userId) // Filtramos por MI ID
            .get()
            .addOnSuccessListener { result ->
                val myHorsesList = result.toObjects(HorseEntity::class.java)
                Log.d("GurryStable", "Encontrados ${myHorsesList.size} caballos en el establo")

                viewModelScope.launch {
                    // Guardamos en Room.
                    // Nota: Room usará "OnConflictStrategy.REPLACE", así que actualiza los existentes
                    // y añade los nuevos (los comprados).
                    if (myHorsesList.isNotEmpty()) {
                        horseDao.insertAll(myHorsesList)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("GurryStable", "Error cargando establo", e)
            }
    }

    // Función extra: Refrescar manualmente (útil para "Pull to refresh")
    fun refreshStable() {
        auth.currentUser?.uid?.let { fetchMyHorsesFromFirebase(it) }
    }
}