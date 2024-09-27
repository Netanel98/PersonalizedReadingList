package com.example.readingbooks.ui.themes.profile

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.readingbooks.MainActivity
import com.example.readingbooks.R
import com.example.readingbooks.data.UserDatabase
import com.example.readingbooks.data.repositories.UserRepository
import com.example.readingbooks.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val firestoreAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userRepository = UserRepository(firestoreDb, firestoreAuth, UserDatabase.getDatabase(requireContext()).userDao())

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this,
            AuthViewModel.AuthModelFactory(userRepository)
        )[AuthViewModel::class.java]

        binding.loginToSignUp.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_signUpFragment)
        }


        mainActivity.hideNavBar()

        binding.btnLogIn.setOnClickListener {
            if(validation()) {
                viewModel.login(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                ) { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.loginSuccessfull.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                mainActivity.displayNavBar()
                Navigation.findNavController(requireView()).popBackStack(R.id.libraryFragment,false)
            } else {
                Toast.makeText(requireContext(), "Couldn't log you in", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.registerProgress.isVisible = true
                binding.btnLogIn.text = ""
            } else {
                binding.registerProgress.isVisible = false
                binding.btnLogIn.text = getString(R.string.login)
            }
        })

        return binding.root
    }
    private lateinit var viewModel: AuthViewModel
    private val mainActivity: MainActivity
        get() = activity as MainActivity

    fun validation(): Boolean {
        val email = binding.etEmail.text
        val password = binding.etPassword.text

        if (email.isNullOrEmpty()){
            Toast.makeText(requireContext(), getString(R.string.email), Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isNullOrEmpty()){
            Toast.makeText(requireContext(), getString(R.string.password), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    override fun onPause() {
        super.onPause()
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}