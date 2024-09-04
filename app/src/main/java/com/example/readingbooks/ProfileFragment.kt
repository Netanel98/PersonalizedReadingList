package com.example.readingbooks

import android.os.Bundle 
import androidx.appcompat.app.AppCompatActivity
import com.example.readingbooks.services.AuthService
import com.example.readinglist.R
import com.example.readingbooks.databinding.ActivityProfileBinding
import com.example.readingbooks.viewModels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Assuming ViewModel setup is done here
        // ViewModel observers
        viewModel.userName.observe(this) { name ->
            binding.etName.setText(name)
        }

        viewModel.photoUrl.observe(this) { url ->
            binding.etPhotoUrl.setText(url)
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val photoUrl = binding.etPhotoUrl.text.toString()
            viewModel.updateUserProfile(name, photoUrl)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logOut()
            // Navigate back to login screen or adjust UI accordingly
        }
    }
}