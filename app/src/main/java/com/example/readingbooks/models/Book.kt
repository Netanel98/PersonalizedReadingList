package com.example.readingbooks.models

import com.bumptech.glide.load.model.GlideUrl
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var imageUrl: String,
    var title: String,
    var subtitle: String,
    var author: String,
    var publisher: String,
    var publishedDate: String,
    var description: String,
    var pageCount: Int,
    var thumbnail: String,
    var previewLink: String
) : Parcelable