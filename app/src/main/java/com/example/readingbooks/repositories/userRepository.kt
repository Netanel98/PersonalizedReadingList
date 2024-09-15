package com.example.readingbooks.repositories


import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.readingbooks.data.AppDatabase
import com.example.readingbooks.data.UserDao
import com.example.readingbooks.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class UserRepository(private val context: Context) {
    companion object {
        const val USERS_COLLECTION = "users"
        const val IMAGES_REF = "images"
    }

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val localDb = AppDatabase.getDatabase(context)
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val imageRepository = ImageRepository(context)

    suspend fun saveUserInDB(user: User) {
        val newUser = user.copy()
        newUser.imageUri = null

        db.collection(USERS_COLLECTION)
            .document(newUser.uid)
            .set(newUser.json)
            .await()

        localDb.UserDao().insertAll(newUser)
    }

    suspend fun saveUserImage(imageUri: String, userId: String) =
        imageRepository.uploadImage(imageUri.toUri(), userId)

    suspend fun getUserById(userId: String): User? {
        var user = localDb.UserDao().getUserById(userId)

        if (user != null) return user.apply { imageUri = imageRepository.getImagePathById(userId) };

        user = getUserFromFireStore(userId)
        localDb.UserDao().insertAll(user)

        return user.apply { imageUri = imageRepository.getImagePathById(userId) }
    }

    suspend fun getUserFromFireStore(userId: String): User {
        val user = db.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)

        user?.uid = userId
        user?.imageUri = imageRepository.downloadAndCacheImage(imageRepository.getImageRemoteUri(userId), userId)

        return user!!
    }

    private suspend fun getUserImageUri(userId: String): Uri = imageRepository.getImageRemoteUri(userId)

    fun userLiveData(userId: String): LiveData<User> {
        val data = MutableLiveData<User>()
        db.collection(USERS_COLLECTION).document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Log or handle the error
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject<User>()
                    user?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            data.value = it
                        }
                    }
                } else {
                    // Handle the case where user data is not available
                }
            }
        return data
}
    }