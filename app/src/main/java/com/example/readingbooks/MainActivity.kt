package com.example.readingbooks

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.readingbooks.data.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var navigationMenu: BottomNavigationView
    lateinit var localDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        localDb = AppDatabase.getDatabase(applicationContext)
        navigationMenu = findViewById(R.id.bottom_navigation)
        setupNavigationMenu()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupNavigationMenu() {
        val navController = getNavController()
        navigationMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.libraryFragment -> {
                    navController.navigate(R.id.libraryFragment)
                    true
                }
                R.id.MyBookListFragment -> {
                    navController.navigate(R.id.MyBookListFragment)
                    true
                }
                else -> false
            }
        }

        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                navigationMenu.visibility = View.GONE
                navController.navigate(R.id.loginFragment)
            } else {
                navigationMenu.visibility = View.VISIBLE
                // Make sure the initial selected item doesn't trigger unnecessary navigation:
                navigationMenu.selectedItemId = R.id.libraryFragment
            }
        }
    }

    fun getNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHost.navController
    }
}