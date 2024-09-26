package com.example.readingbooks.data.repositories

import androidx.lifecycle.LiveData
import com.example.readingbooks.mybooks.BookModal
import com.example.readingbooks.data.dao.BookDao
import com.example.readingbooks.services.FirestoreService

class BookRepository(private val bookDao: BookDao, private val firestoreService: FirestoreService) {

    // Get all books from the local database
    // Assuming BookDao has a method to fetch all books as LiveData
    fun getAllBooks(): LiveData<List<BookModal>> {
        return bookDao.getAllBooks()
    }

    fun searchBooks(query: String): LiveData<List<BookModal>> {
        // Assuming you have a LIKE query in BookDao for searching
        return bookDao.searchBooks("%$query%")
    }

    // Add a new book to both local and remote databases
    suspend fun addBook(book: BookModal) {
        try {
            val bookId = firestoreService.addBook(book)  // Assuming this returns the ID and can throw an exception
            book.id = bookId  // Set the book ID after remote insertion
            bookDao.insertAllBooks(book)  // Ensure DAO supports this operation for a single book
        } catch (e: Exception) {
            // Handle exceptions
            throw RuntimeException("Failed to add book: ${e.message}", e)
        }
    }

    // Update a book
    suspend fun updateBook(book: BookModal) {
        firestoreService.updateBook(book)
        bookDao.updateBook(book)
    }

    // Delete a book
    suspend fun deleteBook(book: BookModal) {
        firestoreService.deleteBook(book.id.toString())
        bookDao.deleteBook(book)
    }
}