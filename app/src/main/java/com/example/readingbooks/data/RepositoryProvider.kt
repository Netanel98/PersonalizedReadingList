package com.example.readingbooks.data

import android.content.Context
import com.example.readingbooks.data.AppDatabase
import com.example.readingbooks.data.dao.BookDao
import com.example.readingbooks.data.repositories.BookRepository
import com.example.readingbooks.services.FirestoreService

/**
 * Provides instances of Repository classes.
 */
object RepositoryProvider {

    @Volatile
    private var bookRepository: BookRepository? = null
    private lateinit var firestoreService: FirestoreService  // Assuming FirestoreService is a singleton or appropriately scoped

    // Optionally pass the context here if required to ensure it's always using the latest context.
    fun provideBookRepository(context: Context): BookRepository {
        return bookRepository ?: synchronized(this) {
            val newRepo = BookRepository(AppDatabase.getDatabase(context).BookDao(), getFirestoreService(context))
            bookRepository = newRepo
            newRepo
        }
    }

    // If FirestoreService needs the context
    private fun getFirestoreService(context: Context): FirestoreService {
        if (!::firestoreService.isInitialized) {
            firestoreService = FirestoreService()
        }
        return firestoreService
    }

    // Implement similar methods for other repositories if needed
}