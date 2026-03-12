package com.example.gurry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val uid: String = "",
    @ColumnInfo(name = "username")
    val username: String = "",
    @ColumnInfo(name = "email")
    val email: String = "",
    @ColumnInfo(name = "balance")
    val balance: Float = 0.0f,
    @ColumnInfo(name = "profile_pic")
    val profilePic: String = "",
    @ColumnInfo(name = "is_registered")
    val isRegistered: Boolean = false,
    @ColumnInfo(name = "cups")
    val cups: Int = 0,
    @ColumnInfo(name = "level")
    val level: Int = 0,
    @ColumnInfo(name = "experience")
    val experience: Int = 0,
)