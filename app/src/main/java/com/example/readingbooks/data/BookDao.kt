package com.example.readingbooks.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.readingbooks.models.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): LiveData<List<Book>>

    @Insert
    fun insertBook(book: Book)

    @Delete
    fun deleteBook(book: Book)

    @Update
    fun updateBook(book: Book)

    @Query("SELECT * FROM books WHERE title LIKE :query OR author LIKE :query")
    fun searchBooks(query: String): LiveData<List<Book>>
}