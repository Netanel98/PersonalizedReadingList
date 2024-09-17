package com.example.readingbooks.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.readingbooks.models.Book
import com.example.readingbooks.repositories.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    fun insert(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.addBook(book)
    }

    fun delete(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteBook(book)
    }
    val allBooks: LiveData<List<Book>> = repository.getAllBooks()

    fun searchBooks(query: String) = liveData {
        emit(repository.searchBooks(query))
    }
}