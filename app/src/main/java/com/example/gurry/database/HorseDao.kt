package com.example.gurry.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {

    // Devuelve un Flow: la UI se actualizará sola si llegan nuevos caballos
    @Query("SELECT * FROM horses_table WHERE id_owner = '0'") // 0 = Caballos del mercado
    fun getMarketHorses(): Flow<List<HorseEntity>>

    @Query("SELECT * FROM horses_table WHERE id_owner = :userId")
    fun getMyHorses(userId: String): Flow<List<HorseEntity>>

    @Query("UPDATE horses_table SET id_owner = :newOwnerId WHERE metadata_id = :horseId")
    suspend fun updateOwner(horseId: String, newOwnerId: String)

    // Insertar o Actualizar una lista entera (Bulk insert)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(horses: List<HorseEntity>)

    @Query("DELETE FROM horses_table WHERE id_owner = '0'")
    suspend fun clearMarketHorses()

    // Borrar todo (útil para refrescar la caché desde cero)
    @Query("DELETE FROM horses_table")
    suspend fun clearHorses()

    // Usar una transacción para evitar que la UI parpadee o se quede vacía
    @Transaction
    suspend fun refreshMarket(horses: List<HorseEntity>) {
        clearMarketHorses()
        insertAll(horses)
    }
}