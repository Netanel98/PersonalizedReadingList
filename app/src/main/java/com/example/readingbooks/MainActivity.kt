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
    private lateinit var cancelButton: FloatingActionButton
    private lateinit var addButton: FloatingActionButton

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
        cancelButton = binding.cancelBotton
        addButton = binding.addBotton

        NavigationUI.setupWithNavController(
            bottomNavigationView,navController
        )

        if (isLoggedin()) {
            navController.navigate(R.id.libraryFragment)
        } else {
            navController.navigate(R.id.loginFragment)
        }

        val cancelButton: FloatingActionButton = binding.cancelBotton
        val addButton: FloatingActionButton = binding.addBotton

        addButton.setOnClickListener {
            navController.navigate(R.id.libraryFragment)
            disableNavBar()
        }

        cancelButton.setOnClickListener {
            navController.navigate(R.id.libraryFragment)
            enableNavBar()
        }
    }
    public override fun onStop() {
        super.onStop()
        scope.cancel()
    }

    fun isLoggedin(): Boolean {
        return auth.currentUser != null
    }

    fun disableNavBar() {
        Handler(Looper.getMainLooper()).post {
            cancelButton.isVisible = true
            addButton.isVisible = false
        }

        val size = bottomNavigationView.menu.size()
        for (i in 0 until size) {
            bottomNavigationView.menu.getItem(i).isChecked = false
            bottomNavigationView.menu.getItem(i).isEnabled = false
        }

        val menuItemDashboard = bottomNavigationView.menu.findItem(R.id.fab)
        menuItemDashboard.isChecked = true
    }

    fun enableNavBar() {
        cancelButton.isVisible = false
        addButton.isVisible = true

        val size = bottomNavigationView.menu.size()
        for (i in 0 until size) {size
            bottomNavigationView.menu.getItem(i).isEnabled = true
        }
    }

    fun hideNavBar() {
        binding.addBotton.isVisible = false
        binding.bottomAppBar.isVisible = false
    }

    fun displayNavBar() {
        binding.addBotton.isVisible = true
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


