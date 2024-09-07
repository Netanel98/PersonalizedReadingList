package com.example.readingbooks.views

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.readingbooks.ProfileActivity


class ImagePicker(private val fragment: ProfileActivity<Any>, private val onImagePicked: (Any?) -> Unit) {

    private val pickImageResultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Here we just handle the result and call onImagePicked with the URI of the selected image
            val imageUri: Any? = result.data?.data
            onImagePicked(imageUri)
        } else {
            // Handle cancellation or failure
            onImagePicked(null)
        }
    }

    fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        pickImageResultLauncher.launch(intent)
    }
}