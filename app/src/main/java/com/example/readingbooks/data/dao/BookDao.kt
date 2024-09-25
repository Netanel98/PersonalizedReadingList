package com.example.readingbooks.data.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE :searchQuery OR author LIKE :searchQuery")
    fun searchBooks(searchQuery: String?): LiveData<List<Book>>

    // Adjusted to use REPLACE strategy for consistency with MovieDao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(book: Book)

    @Delete
    fun deleteBook(book: Book)

    @Update
    fun updateBook(book: Book)

}