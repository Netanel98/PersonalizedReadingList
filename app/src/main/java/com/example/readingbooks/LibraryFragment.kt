package com.example.readingbooks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.readingbooks.R
import com.example.readingbooks.mybooks.BookAdapter
import com.example.readingbooks.mybooks.BookDetailsActivity
import com.example.readingbooks.mybooks.BookModal
import org.json.JSONObject

class LibraryFragment : Fragment() {

    private lateinit var mRequestQueue: RequestQueue
    private lateinit var booksList: ArrayList<BookModal>
    private lateinit var loadingPB: ProgressBar
    private lateinit var searchEdt: EditText
    private lateinit var searchBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        loadingPB = view.findViewById(R.id.idLoadingPB)
        searchEdt = view.findViewById(R.id.idEdtSearchBooks)
        searchBtn = view.findViewById(R.id.idBtnSearch)

        searchBtn.setOnClickListener {
            loadingPB.visibility = View.VISIBLE
            if (searchEdt.text.toString().isEmpty()) {
                searchEdt.error = "Please enter search query"
            } else {
                getBooksData(searchEdt.text.toString())
            }
        }
        return view
    }

    private fun getBooksData(searchQuery: String) {
        booksList = ArrayList()
        mRequestQueue = Volley.newRequestQueue(requireContext())
        mRequestQueue.cache.clear()
        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.visibility = View.GONE
            try {
                val itemsArray = response.getJSONArray("items")
                for (i in 0 until itemsArray.length()) {
                    val bookInfo = extractBookInfo(itemsArray.getJSONObject(i))
                    booksList.add(bookInfo)
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Error parsing book data.", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(context, "No books found.", Toast.LENGTH_SHORT).show()
        })
        setupRecyclerView()
        mRequestQueue.add(request)
    }

    private fun extractBookInfo(jsonObject: JSONObject): BookModal {
        val volumeInfo = jsonObject.getJSONObject("volumeInfo")
        return BookModal(
            jsonObject.optString("id"),
            volumeInfo.optString("title"),
            volumeInfo.optString("subtitle"),
            volumeInfo.optString("authors"),
            volumeInfo.optString("publisher"),
            volumeInfo.optString("publishedDate"),
            volumeInfo.optString("description"),
            volumeInfo.optInt("pageCount"),
            volumeInfo.optJSONObject("imageLinks").optString("thumbnail"),
            volumeInfo.optString("previewLink"),
            volumeInfo.optString("infoLink")
        )
    }

    private fun setupRecyclerView() {
        val adapter = BookAdapter(booksList, requireContext()) { book ->
            val intent = Intent(activity, BookDetailsActivity::class.java).apply {
                putExtra("title", book.title)
                putExtra("subtitle", book.subtitle)
                putExtra("authors", book.author)
                putExtra("publisher", book.publisher)
                putExtra("publishedDate", book.publishedDate)
                putExtra("description", book.description)
                putExtra("pageCount", book.pageCount)
                putExtra("thumbnail", book.thumbnail)
                putExtra("previewLink", book.previewLink)
                putExtra("infoLink", book.infoLink)
                // add all other necessary extras
            }
            startActivity(intent)
        }
        val layoutManager = GridLayoutManager(context, 3)
        view?.findViewById<RecyclerView>(R.id.idRVBooks)?.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
    }
}