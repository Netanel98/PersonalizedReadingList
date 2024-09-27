package com.example.readingbooks.mybooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.readingbooks.data.repositories.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    fun insert(book: BookModal) = viewModelScope.launch(Dispatchers.IO) {
        repository.addBook(book)
    }

    fun delete(book: BookModal) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteBook(book)
    }
    val allBooks: LiveData<List<BookModal>> = repository.getAllBooks()

    fun searchBooks(query: String) = liveData {
        emit(repository.searchBooks(query))
    }
}