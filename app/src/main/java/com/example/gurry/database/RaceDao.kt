package com.example.gurry.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

// 3. El POJO (Plain Old Java Object)
// Es solo una clase de datos para "moldear" el resultado de la consulta.
// VA AQUÍ porque es el DAO quien lo "fabrica".
data class UserRaceHistoryItem(
    val raceId: String,
    val timestamp: Long,
    val horseId: String,
    val finishPosition: Int,
    val prizeWon: Float
)

@Dao
interface RaceDao {

    // Insertar datos (transaccional para asegurar integridad)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRace(race: RaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<RaceParticipantEntity>)

    @Transaction
    suspend fun saveFullRace(race: RaceEntity, participants: List<RaceParticipantEntity>) {
        insertRace(race)
        insertParticipants(participants)
    }

    // CONSULTA MAESTRA: Historial del usuario
    // Devuelve directamente la lista de items formateada
    @Query("""
        SELECT 
            r.raceId, 
            r.timestamp, 
            rp.horseId, 
            rp.finishPosition, 
            rp.prizeWon
        FROM races r
        INNER JOIN race_participants rp ON r.raceId = rp.raceId
        WHERE rp.userId = :userId
        ORDER BY r.timestamp DESC
    """)
    fun getUserRaceHistory(userId: String): Flow<List<UserRaceHistoryItem>>
}