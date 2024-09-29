package com.example.readingbooks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.readingbooks.mybooks.BookModal
import com.example.readingbooks.data.dao.BookDao
import com.example.readingbooks.data.dao.ImageDao
import com.example.readingbooks.data.dao.UserDao
import com.example.readingbooks.models.ImageModel
import com.example.readingbooks.models.User

@Database(entities = [User::class, BookModal::class, ImageModel::class, Review::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun BookDao(): BookDao
    abstract fun UserDao(): UserDao
    abstract fun ImageDao(): ImageDao
    abstract fun ReviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reading_list_database"
                ).fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
