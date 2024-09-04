package com.example.readingbooks.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingbooks.services.AuthService
import kotlinx.coroutines.launch

class ProfileViewModel(private val authService: AuthService) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _photoUrl = MutableLiveData<String>()
    val photoUrl: LiveData<String> = _photoUrl

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = authService.getCurrentUser()
        _userName.value = user?.displayName ?: ""
        _photoUrl.value = user?.photoUrl?.toString() ?: ""
    }

    fun updateUserProfile(name: String, photoUrl: String) {
        viewModelScope.launch {
            authService.updateUserProfile(name, photoUrl) { success, message ->
                if (success) {
                    _userName.value = name
                    _photoUrl.value = photoUrl
                    _statusMessage.value = "Profile Updated Successfully"
                } else {
                    _statusMessage.value = message
                }
            }
        }
    }

    fun logOut() {
        authService.logOut()
    }
}