package com.example.readingbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.readingbooks.data.RepositoryProvider
import com.example.readingbooks.data.repositories.BookRepository

class BookDetailsViewModelFactory(private val context: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailsViewModel::class.java)) {
            return BookDetailsViewModel(RepositoryProvider.provideBookRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}