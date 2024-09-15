package com.example.readingbooks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.readingbooks.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        val navController = findNavController(R.id.nav_host_fragment)
        if (currentUser != null) {
            // Navigate to the book list if already logged in
            navController.navigate(R.id.action_global_bookListFragment)
        }
    }
}


