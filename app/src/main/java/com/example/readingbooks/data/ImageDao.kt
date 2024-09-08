package com.example.readingbooks.data


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.readingbooks.models.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE id = :imageId")
    open fun getImageById(imageId: String?): LiveData<Image?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg images: Image)

    @Query("DELETE FROM images WHERE id = :id")
    fun deleteImage(id: String)
}