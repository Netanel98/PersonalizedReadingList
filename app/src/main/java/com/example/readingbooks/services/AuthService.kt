package com.example.readingbooks.services

import com.google.firebase.auth.FirebaseAuth

class AuthService {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Sign up a new user
    fun signUp(email: String, password: String, callback: (Boolean, String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Signup successful")
                } else {
                    callback(false, task.exception?.message ?: "Signup failed")
                }
            }
    }

    // Log in an existing user
    fun logIn(email: String, password: String, callback: (Boolean, String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Logged in Successfully")
                } else {
                    callback(false, task.exception?.message ?: "Login Failed")
                }
            }
    }

    // Log out the current user
    fun logOut() {
        firebaseAuth.signOut()
    }

    // Get current user
    fun getCurrentUser() = firebaseAuth.currentUser

}