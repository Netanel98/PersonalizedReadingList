package com.example.readingbooks.repositories

import androidx.lifecycle.LiveData
import com.example.readingbooks.data.BookDao
import com.example.readingbooks.models.Book

class BookRepository(private val bookDao: BookDao) {
    val allBooks: LiveData<List<Book>> = bookDao.getAllBooks()

    suspend fun insert(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun delete(book: Book) {
        bookDao.deleteBook(book)
    }
}