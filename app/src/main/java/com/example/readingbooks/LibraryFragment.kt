package com.example.readingbooks

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
import com.example.readingbooks.mybooks.BookModal

class LibraryFragment : Fragment() {

    // Creating variables.
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var booksList: ArrayList<BookModal>
    private lateinit var loadingPB: ProgressBar
    private lateinit var searchEdt: EditText
    private lateinit var searchBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_main, container, false)

        // Initializing our variables with their IDs.
        loadingPB = view.findViewById(R.id.idLoadingPB)
        searchEdt = view.findViewById(R.id.idEdtSearchBooks)
        searchBtn = view.findViewById(R.id.idBtnSearch)

        // Adding click listener for search button
        searchBtn.setOnClickListener {
            loadingPB.visibility = View.VISIBLE
            // Checking if our EditText field is empty.
            if (searchEdt.text.toString().isNullOrEmpty()) {
                searchEdt.error = "Please enter search query"
            } else {
                // If the search query is not empty, we call the method to load books from the API.
                getBooksData(searchEdt.text.toString())
            }
        }
        return view
    }

    private fun getBooksData(searchQuery: String) {
        // Creating a new ArrayList.
        booksList = ArrayList()

        // Initializing the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(requireContext())

        // Clearing cache when our data is being updated.
        mRequestQueue.cache.clear()

        // The URL for getting data from API in JSON format.
        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"

        // Creating a new request queue.
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.visibility = View.GONE
            // Extracting JSON data inside the onResponse method.
            try {
                val itemsArray = response.getJSONArray("items")
                for (i in 0 until itemsArray.length()) {
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")
                    val id = itemsObj.optString("id")
                    val title = volumeObj.optString("title")
                    val subtitle = volumeObj.optString("subtitle")
                    val author = volumeObj.optString("author")
                    val publisher = volumeObj.optString("publisher")
                    val publishedDate = volumeObj.optString("publishedDate")
                    val description = volumeObj.optString("description")
                    val pageCount = volumeObj.optInt("pageCount")
                    val imageLinks = volumeObj.optJSONObject("imageLinks")
                    val thumbnail = imageLinks.optString("thumbnail")
                    val previewLink = volumeObj.optString("previewLink")
                    val infoLink = volumeObj.optString("infoLink")

                    val bookInfo = BookModal(id, title, subtitle, author, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink)
                    booksList.add(bookInfo)
                }
                // Setting up the RecyclerView.
                val adapter = BookAdapter(booksList, requireContext())
                val layoutManager = GridLayoutManager(context, 3)
                val mRecyclerView = view?.findViewById<RecyclerView>(R.id.idRVBooks)
                mRecyclerView?.layoutManager = layoutManager
                mRecyclerView?.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, { error ->
            Toast.makeText(context, "No books found..", Toast.LENGTH_SHORT).show()
        })
        mRequestQueue.add(request)
    }
}