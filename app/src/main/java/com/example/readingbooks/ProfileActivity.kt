package com.example.readingbooks

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.readingbooks.services.AuthService
import com.example.myapplication.R
import com.example.readingbooks.viewModels.ProfileViewModel
import com.example.readingbooks.views.ImagePicker

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var imagePicker: ImagePicker  // Move declaration to class level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)  // Make sure this layout is correct for an activity

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]  // Simplified ViewModel fetching

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

        val userId = AuthService.getCurrentUser().uid ?: ""
        viewModel.loadUserData(userId)

        imagePicker = ImagePicker(this) { uri ->  // Initialize imagePicker here
            uri?.let {
                displayImage(it)  // Ensure correct type usage
            } ?: run {
                showToast("Image picking cancelled or failed.")
            }
        }

        findViewById<Button>(R.id.pickImageButton).setOnClickListener {
            imagePicker.pickImage()
        }
    }

    private fun displayImage(imageUri: Any) {  // Correct type from Any to Uri
        // Implement logic to display the image based on URI
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()  // Use 'this' for context as this is an activity
    }
}