package com.example.readingbooks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.readingbooks.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(users: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): User?

}