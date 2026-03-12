package com.example.gurry.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "races")
data class RaceEntity(
    @PrimaryKey
    val raceId: String = "",
    val timestamp: Long = System.currentTimeMillis(), // Para ordenar por fecha
    val enrollPrice: Float = 0.0f,
    val prizePool: Float = 0.0f,
    val winnerId: String? = null, // ID del ganador para acceso rápido
    val isFinished: Boolean = false
)
@Entity(
    tableName = "race_participants",
    primaryKeys = ["raceId", "userId"], // Clave compuesta
    foreignKeys = [
        ForeignKey(
            entity = RaceEntity::class,
            parentColumns = ["raceId"],
            childColumns = ["raceId"],
            onDelete = ForeignKey.CASCADE // Si borras la carrera, se borran los participantes
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["userId"]
        )
    ]
)
data class RaceParticipantEntity(
    val raceId: String,
    val userId: String,
    val horseId: String,     // Con qué caballo corrió
    val finishPosition: Int, // 1, 2, 3... (0 si no terminó)
    val prizeWon: Float      // Dinero ganado
)