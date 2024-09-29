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
import com.example.readingbooks.mybooks.BookAdapter
import com.example.readingbooks.mybooks.BookModal

class LibraryFragment : Fragment() {

    private lateinit var mRequestQueue: RequestQueue
    private lateinit var booksList: ArrayList<BookModal>
    private lateinit var loadingPB: ProgressBar
    private lateinit var searchEdt: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        loadingPB = view.findViewById(R.id.idLoadingPB)
        searchEdt = view.findViewById(R.id.idEdtSearchBooks)
        searchBtn = view.findViewById(R.id.idBtnSearch)
        mRecyclerView = view.findViewById(R.id.idRVBooks)

        searchBtn.setOnClickListener {
            if (searchEdt.text.toString().isNullOrEmpty()) {
                searchEdt.error = "Please enter search query"
            } else {
                loadingPB.visibility = View.VISIBLE
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
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")
                    val id = itemsObj.optString("id")
                    val title = volumeObj.optString("title")
                    val subtitle = volumeObj.optString("subtitle")
                    val author = volumeObj.optString("authors")
                    val publisher = volumeObj.optString("publisher")
                    val publishedDate = volumeObj.optString("publishedDate")
                    val description = volumeObj.optString("description")
                    val pageCount = volumeObj.optInt("pageCount")
                    val imageLinks = volumeObj.optJSONObject("imageLinks")
                    val thumbnail = imageLinks.optString("thumbnail")
                    val previewLink = volumeObj.optString("previewLink")
                    val infoLink = volumeObj.optString("infoLink")

                    val bookInfo = BookModal(
                        id, title, subtitle, author, publisher, publishedDate,
                        description, pageCount.toString(), thumbnail, previewLink, infoLink
                    )
                    booksList.add(bookInfo)
                }
                setupRecyclerView()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error parsing book data.", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(context, "No books found..", Toast.LENGTH_SHORT).show()
        })

        mRequestQueue.add(request)
    }

    private fun setupRecyclerView() {
        val adapter = BookAdapter(booksList, requireContext())
        mRecyclerView.layoutManager = GridLayoutManager(context, 3)
        mRecyclerView.adapter = adapter
    }
}