package com.example.readingbooks.services

import com.example.readingbooks.models.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addBook(book: Book) {
        db.collection("books").add(book).await()
    }

    suspend fun getBooks(): List<Book> {
        return db.collection("books")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject<Book>() }
    }

    suspend fun updateBook(book: Book) {
        book.id?.let {
            db.collection("books").document(it.toString()).set(book).await()
        }
    }

    suspend fun deleteBook(bookId: String) {
        db.collection("books").document(bookId).delete().await()
    }
}