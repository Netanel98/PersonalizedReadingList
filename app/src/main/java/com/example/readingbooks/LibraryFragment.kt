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
import com.example.readingbooks.models.Book
import com.example.readingbooks.R
import org.json.JSONObject

class LibraryFragment : Fragment() {
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var booksList: ArrayList<Book>
    private lateinit var loadingPB: ProgressBar
    private lateinit var searchEdt: EditText
    private lateinit var searchBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initializing our variables with their ids
        loadingPB = view.findViewById(R.id.idLoadingPB)
        searchEdt = view.findViewById(R.id.idEdtSearchBooks)
        searchBtn = view.findViewById(R.id.idBtnSearch)

        // adding click listener for search button
        searchBtn.setOnClickListener {
            loadingPB.visibility = View.VISIBLE
            // checking if our edittext field is empty or not
            if (searchEdt.text.toString().isNullOrEmpty()) {
                searchEdt.error = "Please enter search query"
                loadingPB.visibility = View.GONE
            } else {
                // if the search query is not empty then we are
                // calling get books data method to load all
                // the books from the API
                getBooksData(searchEdt.text.toString())
            }
        }
    }

    private fun getBooksData(searchQuery: String) {
        // creating a new array list
        booksList = ArrayList()

        // below line is to initialize
        // the variable for our request queue
        mRequestQueue = Volley.newRequestQueue(requireContext())
        mRequestQueue.cache.clear()

        // url for getting data from API in json format
        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"

        // creating a new request queue
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.visibility = View.GONE
            // inside on response method we are extracting all our json data
            try {
                val itemsArray = response.getJSONArray("items")
                for (i in 0 until itemsArray.length()) {
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")
                    val id = itemsObj.getString("id")
                    val imageUrl = volumeObj.optString("imageLinks").let {
                        if (it.isNotEmpty()) JSONObject(it).optString("thumbnail")
                        else ""
                    }
                    val title = volumeObj.optString("title")
                    val subtitle = volumeObj.optString("subtitle")
                    val authorsArray = volumeObj.getJSONArray("authors")
                    val publisher = volumeObj.optString("publisher")
                    val publishedDate = volumeObj.optString("publishedDate")
                    val description = volumeObj.optString("description")
                    val pageCount = volumeObj.optInt("pageCount")
                    val previewLink = volumeObj.optString("previewLink")
                    val infoLink = volumeObj.optString("infoLink")
                    val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                    val buyLink = saleInfoObj?.optString("buyLink")
                    val authorsArrayList: ArrayList<String> = ArrayList()
                    if (authorsArray.length() != 0) {
                        for (j in 0 until authorsArray.length()) {
                            authorsArrayList.add(authorsArray.optString(j))
                        }
                    }

                    val bookInfo = Book(
                        id.toInt(),
                        imageUrl,
                        title,
                        subtitle,
                        authorsArrayList.toString(),
                        publisher,
                        publishedDate,
                        description,
                        pageCount,
                        imageUrl,
                        previewLink,
                        infoLink,
                        buyLink ?: ""
                    )
                    booksList.add(bookInfo)
                }
                val adapter = BookRVAdapter(booksList, requireContext())
                val mRecyclerView = view?.findViewById<RecyclerView>(R.id.idRVBooks)
                mRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
                mRecyclerView?.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, { error ->
            // in this case we are simply displaying a toast message
            Toast.makeText(requireContext(), "Failed to load data..", Toast.LENGTH_SHORT).show()
            loadingPB.visibility = View.GONE
        })
        mRequestQueue.add(request)
    }
}