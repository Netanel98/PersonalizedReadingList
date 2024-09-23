package com.example.readingbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingbooks.models.Book
import com.example.readingbooks.data.repositories.BookRepository
import kotlinx.coroutines.launch

class BookDetailsViewModel(private val bookRepository: BookRepository) : ViewModel() {

    // Function to add a book to the reading list
    fun addBookToReadingList(book: Book) {
        viewModelScope.launch {
            bookRepository.addBook(book)
        }
    }
}