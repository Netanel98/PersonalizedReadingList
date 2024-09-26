package com.example.readingbooks.mybooks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookModal (
    @PrimaryKey var id: String,
    // creating string, int and array list
    // variables for our book details
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "subtitles") var subtitle: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "publisher") var publisher: String,
    @ColumnInfo(name = "publishedDate") var publishedDate: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "pageCount") var pageCount: Int,
    @ColumnInfo(name = "thumbnail") var thumbnail: String,
    @ColumnInfo(name = "previewLink") var previewLink: String,
    @ColumnInfo(name = "infoLink") var infoLink: String,
)
