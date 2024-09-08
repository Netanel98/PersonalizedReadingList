package com.example.readingbooks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.readingbooks.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    open fun getAll(): List<User?>?

    @Query("SELECT * FROM users WHERE id = :userId")
    open fun getUserById(userId: String?): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)
    }