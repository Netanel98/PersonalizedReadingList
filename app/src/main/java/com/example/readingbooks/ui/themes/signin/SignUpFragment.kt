package com.example.readingbooks.ui.themes.signin

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.readingbooks.R
import com.example.readingbooks.data.UserDatabase
import com.example.readingbooks.models.FirestoreUser
import com.example.readingbooks.data.repositories.UserRepository
import com.example.readingbooks.databinding.FragmentSignUpBinding
import com.example.readingbooks.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import android.util.Log
import android.widget.ImageView
import androidx.navigation.Navigation
import com.example.readingbooks.ui.themes.AuthViewModel

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: AuthViewModel
    private val mainActivity: MainActivity
        get() = activity as MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val firestoreAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val profileImageRef: StorageReference = Firebase.storage.reference.child("profileImages")

        val userRepository = UserRepository(firestoreDb, firestoreAuth, UserDatabase.getDatabase(requireContext()).userDao())

        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this,
            AuthViewModel.AuthModelFactory(userRepository)
        )[AuthViewModel::class.java]

        binding.signUpToLogin.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_logInFragment)
        }

        mainActivity.hideNavBar()

        binding.btnSignUp.setOnClickListener {
            if(validation()) {
                viewModel.createUser(
                    FirestoreUser(
                        email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString(),
                        name = binding.etName.text.toString()
                    ), profileImageRef
                ) { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.imageView.setOnClickListener {
            mainActivity.requestPermission.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
        mainActivity.uriResult.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                try {
                    val contentResolver = requireContext().contentResolver
                    val imageView: ImageView = binding.imageView
                    viewModel.ShowImgInView(contentResolver, imageView, uri)
                } catch (e: Exception) {
                    Log.e("Picturerequest", "Error reading exif", e)
                    Toast.makeText(requireContext(), getString(R.string.image_error), Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.signUpSuccessfull.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                mainActivity.displayNavBar()
                mainActivity.apply { uriResult.value = null }
                Navigation.findNavController(requireView()).popBackStack(R.id.libraryFragment,false)
            } else {
                // Handle unsuccessful login
            }
        })

        // Observe the loading LiveData
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.registerProgress.isVisible = true
                binding.btnSignUp.text = ""
            } else {
                binding.registerProgress.isVisible = false
                binding.btnSignUp.text = getString(R.string.sign_up)
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    override fun onPause() {
        super.onPause()
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    fun validation(): Boolean {
        val name = binding.etName.text
        val email = binding.etEmail.text
        val password = binding.etPassword.text

        if (viewModel.imageToShow.value == null) {
            Toast.makeText(requireContext(), getString(R.string.pick_image), Toast.LENGTH_SHORT).show()
            return false
        }

        if (name.isNullOrEmpty()){
            Toast.makeText(requireContext(), getString(R.string.name), Toast.LENGTH_SHORT).show()
            return false
        }

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
}