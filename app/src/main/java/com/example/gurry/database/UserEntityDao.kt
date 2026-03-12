package com.example.gurry.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEntityDao{

    @Query("SELECT * FROM UserEntity WHERE uid = :uid LIMIT 1")
    suspend fun getUserById(uid: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE uid = :uid")
    fun getUserFlow(uid: String): Flow<UserEntity?>

    @Query("UPDATE UserEntity SET balance = :newBalance WHERE uid = :uid")
    suspend fun updateBalance(uid: String, newBalance: Float)

    @Upsert
    suspend fun insertOrUpdateUser(userEntity: UserEntity)

    companion object

}