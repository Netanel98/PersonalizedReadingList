package com.example.readingbooks.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingbooks.models.Book
import com.example.readingbooks.repositories.BookRepository
import kotlinx.coroutines.launch

class MyBookListViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        _isLoading.value = true
        viewModelScope.launch {
            bookRepository.getAllBooks().observeForever { books ->
                _books.value = books
                _isLoading.value = false
            }
        }
    }

    fun refreshBooks() {
        fetchBooks()
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            _isLoading.value = true
            bookRepository.addBook((book))
            fetchBooks()
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            _isLoading.value = true
            bookRepository.deleteBook(book)
            fetchBooks()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Handle any cleanup you might need when the ViewModel is destroyed
    }
}