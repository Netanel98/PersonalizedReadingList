package com.example.readingbooks.mybooks.listbooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingbooks.BookModal
import com.example.readingbooks.data.repositories.BookRepository
import kotlinx.coroutines.launch

class MyBookListViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _books = MutableLiveData<List<BookModal>>()
    val books: LiveData<List<BookModal>> get() = _books

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


    fun addBook(book: BookModal) {
        viewModelScope.launch {
            _isLoading.value = true
            bookRepository.addBook(book)
            _isLoading.value = false
        }
    }

    fun deleteBook(book: BookModal) {
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