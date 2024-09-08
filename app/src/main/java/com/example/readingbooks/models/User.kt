package com.example.readingbooks.models

import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") var uid: String = "",

    @ColumnInfo(name = "email") val email: String = "",

    @ColumnInfo(name = "first_name") val firstName: String = "",

    @ColumnInfo(name = "last_name") val lastName: String = "",

    @ColumnInfo(name = "image_uri") var imageUri: String? = null
) {

    // Derived property to handle remote URL or local URI
    val remoteImageUri: String?
        get() = if (imageUri?.startsWith("http") == true) imageUri else null

    val localImageUri: String?
        get() = if (imageUri?.startsWith("http") == false) imageUri else null

    companion object {
        const val EMAIL_KEY = "email"
        const val FIRST_NAME_KEY = "first_name"
        const val LAST_NAME_KEY = "last_name"
        const val IMAGE_URI_KEY = "image_uri"
    }

    // Function to convert User to a JSON-style map, useful for data transfers or inter-app communication
    val json: Map<String, String?>
        get() = hashMapOf(
            EMAIL_KEY to email,
            FIRST_NAME_KEY to firstName,
            LAST_NAME_KEY to lastName,
            IMAGE_URI_KEY to imageUri
        )
}