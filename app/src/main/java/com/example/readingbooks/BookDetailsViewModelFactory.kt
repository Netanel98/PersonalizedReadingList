package com.example.readingbooks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.readingbooks.data.RepositoryProvider
import com.example.readingbooks.data.repositories.BookRepository

class BookDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailsViewModel(RepositoryProvider.provideBookRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}