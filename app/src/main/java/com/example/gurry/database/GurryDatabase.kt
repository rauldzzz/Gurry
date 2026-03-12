package com.example.gurry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
    UserEntity::class,
    HorseEntity::class,
    RaceEntity::class,
    RaceParticipantEntity::class
                     ],
    version = 3
)
abstract class GurryDatabase : RoomDatabase() {

    abstract fun getUserEntityDao(): UserEntityDao
    abstract fun getHorseDao(): HorseDao
    abstract fun getRaceDao(): RaceDao
    companion object {
        @Volatile
        private var INSTANCE: GurryDatabase? = null

        fun getDatabase(context: Context): GurryDatabase {
            // Si la instancia ya existe, la devolvemos
            return INSTANCE ?: synchronized(this) {
                // Creamos la base de datos de forma segura para Android
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GurryDatabase::class.java,
                    "gurry_database"
                )
                    // Esto evita que la app explote si cambias campos en UserEntity
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}