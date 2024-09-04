package com.example.readingbooks.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val imageUrl: String
)