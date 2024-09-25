package com.example.readingbooks.data.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.readingbooks.BookModal

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): LiveData<List<BookModal>>

    @Query("SELECT * FROM books WHERE title LIKE :searchQuery OR authors LIKE :searchQuery")
    fun searchBooks(searchQuery: String?): LiveData<List<BookModal>>

    // Adjusted to use REPLACE strategy for consistency with MovieDao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(book: BookModal)

    @Delete
    fun deleteBook(book: BookModal)

    @Update
    fun updateBook(book: BookModal)

}