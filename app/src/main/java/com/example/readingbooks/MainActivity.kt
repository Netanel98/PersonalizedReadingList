package com.example.readingbooks

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import com.example.readingbooks.R
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.readingbooks.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val scope = CoroutineScope(Dispatchers.IO + Job())
    var uriResult: MutableLiveData<Uri?> = MutableLiveData<Uri?>()
    val requestPermission =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uriResult.value = uri
                Log.d("Picturerequest", "$uri")
            } else {
                Log.d("Picturerequest", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(
            bottomNavigationView,navController
        )

        if (isLoggedin()) {
            navController.navigate(R.id.libraryFragment)
        } else {
            navController.navigate(R.id.loginFragment)
        }
    }
    public override fun onStop() {
        super.onStop()
        scope.cancel()
    }

    fun isLoggedin(): Boolean {
        return auth.currentUser != null
    }

    fun hideNavBar() {
        binding.bottomAppBar.isVisible = false
    }

    fun displayNavBar() {
        binding.bottomAppBar.isVisible = true
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selectedItemId = bottomNavigationView.selectedItemId
        outState.putInt("SELECTED_ITEM_ID", selectedItemId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedItemId = savedInstanceState.getInt("SELECTED_ITEM_ID", R.id.libraryFragment)
        bottomNavigationView.selectedItemId = selectedItemId
    }
}


