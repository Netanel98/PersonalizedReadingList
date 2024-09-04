package com.example.readingbooks.services

import com.example.readingbooks.models.Book
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    // Add a book to Firestore and return the document ID
    suspend fun addBook(book: Book): String {
        val document = db.collection("books").add(book).await()
        return document.id  // Return the new document ID
    }

    // Update a book in Firestore
    suspend fun updateBook(book: Book) {
        book.id?.let { db.collection("books").document(it.toString()).set(book).await() }
    }

    // Delete a book from Firestore
    suspend fun deleteBook(bookId: String) {
        db.collection("books").document(bookId).delete().await()
    }
}