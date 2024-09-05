package com.example.readingbooks

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.readingbooks.services.AuthService
import com.example.readinglist.R
import com.example.readingbooks.viewModels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        // Assuming you have a ViewModelFactory or other dependency injection setup
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        viewModel.firstName.observe(this) { name ->
            findViewById<EditText>(R.id.etName).setText(name)
        }
        viewModel.lastName.observe(this) { name ->
            findViewById<EditText>(R.id.last_name).setText(name)
        }

        viewModel.imageUri.observe(this) { url ->
            findViewById<EditText>(R.id.image_view).setText(url)
        }

        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.loadUserData()
    }
}