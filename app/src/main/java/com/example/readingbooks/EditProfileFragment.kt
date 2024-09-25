package com.example.readingbooks

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.readingbooks.R
import com.example.readingbooks.data.UserDatabase
import com.example.readingbooks.data.repositories.UserRepository
import com.example.readingbooks.databinding.FragmentEditProfileBinding
import com.example.readingbooks.MainActivity
import com.example.readingbooks.ui.themes.AuthViewModel
import com.example.readingbooks.utils.ImageUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.example.readingbooks.EditProfileFragmentArgs

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: AuthViewModel
    private val mainActivity: MainActivity
        get() = activity as MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val firestoreAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userRepository = UserRepository(firestoreDb, firestoreAuth, UserDatabase.getDatabase(requireContext()).userDao())
        val profileImageRef: StorageReference = Firebase.storage.reference.child("profileImages")

        viewModel = ViewModelProvider(
            this,
            AuthViewModel.AuthModelFactory(userRepository)
        )[AuthViewModel::class.java]

        viewModel.updateCurrUser(firestoreAuth.currentUser!!)

        // Disable nav bar
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val size = bottomNavigationView.menu.size()
        for (i in 0 until size) {
            bottomNavigationView.menu.getItem(i).isEnabled = false
        }

        val name = arguments?.getString("name") ?: ""
        val photoUrl = arguments?.getString("photoUrl") ?: ""

        binding.etName.setText(name)
        ImageUtil.showImgInViewFromUrl(photoUrl, binding.imageView, binding.progressBar)
        
        binding.saveChanges.setOnClickListener {
            val currUserImage = viewModel.currUser.value?.photoUrl
            val displayedName = binding.etName.text.toString()
            val displayedImg = mainActivity.uriResult.value ?: currUserImage
            if(validation(currUserImage, displayedName, displayedImg)) {
                viewModel.updateProfile(displayedName, profileImageRef, displayedImg!!, displayedImg != currUserImage)
            }
        }
        binding.cancle.setOnClickListener {
            for (i in 0 until size) {
                bottomNavigationView.menu.getItem(i).isEnabled = true
            }
            Navigation.findNavController(requireView()).popBackStack(R.id.settingsFragment, false)
        }

        binding.changePic.setOnClickListener{
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

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            Log.d("load", isLoading.toString())
            if (isLoading) {
                binding.updateProgress.isVisible = true
                binding.saveChanges.text = ""
            } else {
                binding.updateProgress.isVisible = false
                binding.saveChanges.text = getString(R.string.save_changes)
            }
        })

        viewModel.updateSuccessfull.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Update successfully", Toast.LENGTH_SHORT).show()
                for (i in 0 until size) {
                    bottomNavigationView.menu.getItem(i).isEnabled = true
                }
                Navigation.findNavController(requireView()).popBackStack(R.id.settingsFragment, false)
            } else {
                Toast.makeText(requireContext(), "Couldn't update your info", Toast.LENGTH_SHORT).show()
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

    fun validation(currUserImage: Uri?, displayedName: String, displayedImg: Uri?): Boolean {
        val currUserName = viewModel.currUser.value?.displayName

        if(displayedImg == currUserImage && displayedName == currUserName){
            Toast.makeText(requireContext(), getString(R.string.no_changes), Toast.LENGTH_SHORT).show()
            return false
        }

        if (displayedName == "") {
            Toast.makeText(requireContext(), getString(R.string.name), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}