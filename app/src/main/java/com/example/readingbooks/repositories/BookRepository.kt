package com.example.readingbooks.repositories

import androidx.lifecycle.LiveData
import com.example.readingbooks.data.BookDao
import com.example.readingbooks.models.Book
import com.example.readingbooks.services.FirestoreService

class BookRepository(private val bookDao: BookDao, private val firestoreService: FirestoreService) {

    // Get all books from the local database
    val allBooks: LiveData<List<Book>> = bookDao.getAllBooks()

    // Add a new book to both local and remote databases
    suspend fun addBook(book: Book) {
        val bookId = firestoreService.addBook(book)  // Assuming this returns the ID
        book.id = bookId  // Set the book ID after remote insertion
        bookDao.insertBook(book)
    }

    // Update a book
    suspend fun updateBook(book: Book) {
        firestoreService.updateBook(book)
        bookDao.updateBook(book)
    }

    // Delete a book
    suspend fun deleteBook(book: Book) {
        firestoreService.deleteBook(book.id.toString())
        bookDao.deleteBook(book)
    }
}