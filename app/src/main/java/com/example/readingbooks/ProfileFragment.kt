package com.example.readingbooks

import android.os.Bundle 
import androidx.appcompat.app.AppCompatActivity
import com.example.readingbooks.services.AuthService
import com.example.readinglist.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileFragment : AppCompatActivity() {

        private lateinit var authService: AuthService

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_profile)
            authService = AuthService()

            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val photoUrl = etPhotoUrl.text.toString()  // Assume input is a valid URL or handle conversion
                authService.updateUserProfile(name, photoUrl) { success, message ->
                    if (success) {
                        // Update UI or notify user
                    } else {
                        // Handle error, show message
                    }
                }
            }

            btnLogout.setOnClickListener {
                authService.logOut()
                // Handle UI change or navigate to login screen
            }
        }
    }