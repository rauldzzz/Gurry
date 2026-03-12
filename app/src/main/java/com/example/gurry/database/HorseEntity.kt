package com.example.gurry.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horses_table")
data class HorseEntity(
    @PrimaryKey
    val metadata_id: String = "", // El ID base58
    val name: String = "",
    val horse_img: String = "",
    val display_3d: String = "",
    val id_owner: String = "0", // 0 = Mercado
    val color: String = "",
    val score: Float = 0.0f,
    val price: Float = 0.0f,
    // Stats detalladas
    val gallop: Float = 0.0f,
    val snatch: Float = 0.0f,
    val endurance: Float = 0.0f,
    val acceleration: Float = 0.0f,
    val is_racing: Boolean = false
) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "", "", "0", "", 0f, 0f, 0f, 0f, 0f, 0f, false)
}