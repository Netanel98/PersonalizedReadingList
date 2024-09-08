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
    open fun getAllBooks(): LiveData<List<Book?>?>?

    @Query("SELECT * FROM books WHERE title LIKE :searchQuery OR author LIKE :searchQuery")
    open fun searchBooks(searchQuery: String?): LiveData<List<Book?>?>?

    @Insert
    fun insertBook(book: Book)

    @Delete
    fun deleteBook(book: Book)

    @Update
    fun updateBook(book: Book)
}