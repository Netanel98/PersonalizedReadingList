package com.example.readingbooks.data.repositories

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.example.readingbooks.data.AppDatabase
import com.example.readingbooks.models.Image
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ImageRepository(private val context: Context) {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val localDb = AppDatabase.getDatabase(context)

    companion object {
        const val IMAGES_REF = "images"
    }

    suspend fun uploadImage(imageUri: Uri, imageId: String) {
        val imageRef = storage.reference.child("$IMAGES_REF/$imageId")
        imageRef.putFile(imageUri).await()

        localDb.ImageDao().insertAllImages(Image(imageId, imageUri.toString()))
    }

    suspend fun getImageRemoteUri(imageId: String): Uri {
        val imageRef = storage.reference.child("$IMAGES_REF/$imageId")

        return imageRef.downloadUrl.await()
    }

    fun downloadAndCacheImage(uri: Uri, imageId: String): String {
        val file = Glide.with(context)
            .asFile()
            .load(uri)
            .submit()
            .get()

        localDb.ImageDao().insertAllImages(Image(imageId, file.absolutePath))

        return file.absolutePath
    }

    fun getImageLocalUri(imageId: String): String {
        return localDb.ImageDao().getImageById(imageId).value?.uri ?: ""
    }

    suspend fun getImagePathById(imageId: String): String {
        val image = localDb.ImageDao().getImageById(imageId).value

        if (image != null) return image.uri

        val remoteUri = getImageRemoteUri(imageId)
        val localPath = downloadAndCacheImage(remoteUri, imageId)

        localDb.ImageDao().insertAllImages(Image(imageId, localPath))

        return localPath
    }

    suspend fun deleteImage(imageId: String) {
        val imageRef = storage.reference.child("$IMAGES_REF/$imageId")
        imageRef.delete().await()

        deleteLocalImage(imageId)
    }

    private fun deleteLocalImage(imageId: String) {
        val image = localDb.ImageDao().getImageById(imageId).value
        image?.let {
            val file = Glide.with(context)
                .asFile()
                .load(it.uri)
                .submit()
                .get()

            if (file.exists()) {
                file.delete()
            }

            localDb.ImageDao().deleteImage(imageId)
        }
    }
}