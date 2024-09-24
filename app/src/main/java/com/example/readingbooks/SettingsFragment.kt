package com.example.readingbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.readingbooks.R
import com.example.readingbooks.data.UserDatabase
import com.example.readingbooks.data.repositories.UserRepository
import com.example.readingbooks.databinding.FragmentSettingsBinding
import com.example.readingbooks.utils.ImageUtil
import com.example.readingbooks.ui.themes.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val firestoreAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userRepository = UserRepository(firestoreDb, firestoreAuth, UserDatabase.getDatabase(requireContext()).userDao())

        viewModel = ViewModelProvider(
            this,
            AuthViewModel.AuthModelFactory(userRepository)
        )[AuthViewModel::class.java]

        viewModel.updateCurrUser(firestoreAuth.currentUser!!)

        binding.signout.setOnClickListener {
            viewModel.logOut()
            Navigation.findNavController(requireView()).navigate(R.id.action_settingsFragment_to_logInFragment)
        }

        binding.editUser.setOnClickListener(
            viewModel.currUser.value?.let {user ->
                Navigation.createNavigateOnClickListener(
                    SettingsFragmentDirections.actionSettingsFragmentToEditProfileFragment(
                        user.displayName!!,
                        user.photoUrl.toString()
                    )
                )
            }

        )

        viewModel.currUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                val imageView: ImageView = binding.imageView
                val progressBar: ProgressBar = binding.progressBar
                ImageUtil.showImgInViewFromUrl(user.photoUrl.toString(), imageView, progressBar)
                binding.emailtext.text = user.email
                binding.userName.text = user.displayName
            }
        }

        return binding.root
    }
}