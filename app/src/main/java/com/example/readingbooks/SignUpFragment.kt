package com.example.readingbooks


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.readingbooks.R
import com.example.readingbooks.repositories.UserRepository
import com.example.readingbooks.databinding.FragmentSignUpBinding
import com.example.readingbooks.views.ImagePicker
import com.example.readingbooks.utils.BasicAlert
import com.example.readingbooks.viewModels.SignUpViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.yalantis.ucrop.UCrop
import java.io.File

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val userRepository: UserRepository by lazy { UserRepository(requireContext()) }
    private val viewModel: SignUpViewModel by viewModels { SignUpViewModelFactory(userRepository) }
    lateinit var loginLink: View
    lateinit var signUpButton: Button
    lateinit var imageView: ImagePicker
    lateinit var progressBar: ProgressBar
    private val imagePicker: ActivityResultLauncher<String> = getImagePicker()
    private val uCropLauncher: ActivityResultLauncher<Intent> = getUCropLauncher()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSignUpBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container, false
        )
        bindViews(binding)

        setupUploadButton(binding)
        setupLoginLink(binding)
        setupSignUpButton(binding)
        imageView.requestStoragePermission(requireContext(), requireActivity())

        return binding.root
    }

    private fun bindViews(binding: FragmentSignUpBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupUploadButton(binding: FragmentSignUpBinding) {
        imageView = binding.root.findViewById(R.id.image_view)
        imageView.setOnClickListener {
            imagePicker.launch("image/*")
        }
        addOnIsImageUriValidChangedCallback()
    }

    private fun addOnIsImageUriValidChangedCallback() {
        viewModel.isImageUriValid.observe(viewLifecycleOwner) {
            if (!viewModel.isImageUriValid.value!!) {
                BasicAlert("Invalid Input", "Please upload an image", requireContext()).show()
            }
        }
    }

    private fun setupLoginLink(binding: FragmentSignUpBinding) {
        loginLink = binding.root.findViewById(R.id.login_link)
        loginLink.setOnClickListener {
            findNavController().navigate(R.id.signUp_to_login)
        }
    }

    private fun setupSignUpButton(binding: FragmentSignUpBinding) {
        signUpButton = binding.root.findViewById(R.id.signUp_button)
        progressBar = binding.root.findViewById(R.id.progress_bar)
        signUpButton.setOnClickListener {
            showProgressBar()
            viewModel.signingUp({ onSignUpSuccess() }, { error -> onSignUpFailure(error) })
        }
    }

    private fun onSignUpSuccess() {
        BasicAlert("Success", "You have successfully Signed Up.", requireContext()).show()
    }

    private fun onSignUpFailure(error: Exception?) {
        if (error == null) {
            signUpButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            return
        }

        Log.e("Sign Up", "Error Signing Up", error)
        handleSignUpError(error)
        showSignUpButton()
    }

    private fun handleSignUpError(error: Exception) {
        when (error) {
            is FirebaseAuthUserCollisionException -> {
                BasicAlert(
                    "Sign Up Error",
                    "User with this email already exists",
                    requireContext()
                ).show()
            }

            else -> {
                BasicAlert("Sign Up Error", "An error occurred", requireContext()).show()
            }
        }
    }

    private fun showSignUpButton() {
        signUpButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        signUpButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun getImagePicker() =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageView.getImagePicker(uri, uCropLauncher)
        }

    private fun getUCropLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = UCrop.getOutput(result.data!!)
                imageView.setImageURI(uri)
                viewModel.imageUri.value = uri.toString()
            }
        }
}