package com.example.readingbooks.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.load.model.GlideUrl

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var imageUrl: String,
    var title: String,
    var subtitle: String,
    var author: ArrayList<String>,
    var publisher: String,
    var publishedDate: String,
    var description: String,
    var pageCount: Int,
    var thumbnail: String,
    var previewLink: String,
    var infoLink: String,
    var buyLink: String
)