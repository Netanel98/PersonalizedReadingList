package com.example.readingbooks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.readingbooks.models.Book
import com.example.readingbooks.models.Image
import com.example.readingbooks.models.User

@Database(entities = [User::class, Book::class, Image::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun BookDao(): BookDao
    abstract fun UserDao(): UserDao
    abstract fun ImageDao(): ImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reading_list_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
