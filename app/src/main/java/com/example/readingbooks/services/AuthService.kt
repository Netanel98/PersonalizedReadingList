package com.example.readingbooks.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthService {
    private val auth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, "Sign Up Successful")
            } else {
                onComplete(false, task.exception?.message ?: "Sign Up Failed")
            }
        }
    }

    fun logIn(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, "Login Successful")
            } else {
                onComplete(false, task.exception?.message ?: "Login Failed")
            }
        }
    }

    fun updateUserProfile(name: String, photoUrl: String, onComplete: (Boolean, String) -> Unit) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(android.net.Uri.parse(photoUrl))
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, "Profile Updated Successfully")
            } else {
                onComplete(false, task.exception?.message ?: "Profile Update Failed")
            }
        }
    }

    fun logOut() {
        auth.signOut()
    }
}