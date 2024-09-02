package com.example.readingbooks

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.readinglist.R

class MainActivity : AppCompatActivity() {

    // on below line we are creating variables.
    lateinit var mRequestQueue: RequestQueue
    lateinit var booksList: ArrayList<BookRVModal>
    lateinit var loadingPB: ProgressBar
    lateinit var searchEdt: EditText
    lateinit var searchBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are initializing
        // our variable with their ids.
        loadingPB = findViewById(R.id.idLoadingPB)
        searchEdt = findViewById(R.id.idEdtSearchBooks)
        searchBtn = findViewById(R.id.idBtnSearch)

        // adding click listener for search button
        searchBtn.setOnClickListener {
            loadingPB.visibility = View.VISIBLE
            // checking if our edittext field is empty or not.
            if (searchEdt.text.toString().isNullOrEmpty()) {
                searchEdt.setError("Please enter search query")
            }
            // if the search query is not empty then we are
            // calling get book info method to load all
            // the books from the API.
            getBooksData(searchEdt.getText().toString());
        }

    }

    private fun getBooksData(searchQuery: String) {
        // creating a new array list.
        booksList = ArrayList()

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(this@MainActivity)

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.cache.clear()

        // below is the url for getting data from API in json format.
        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"

        // below line we are  creating a new request queue.
        val queue = Volley.newRequestQueue(this@MainActivity)

        // on below line we are creating a variable for request
        // and initializing it with json object request
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.setVisibility(View.GONE);
            // inside on response method we are extracting all our json data.
            try {
                val itemsArray = response.getJSONArray("items")
                for (i in 0 until itemsArray.length()) {
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")
                    val title = volumeObj.optString("title")
                    val subtitle = volumeObj.optString("subtitle")
                    val authorsArray = volumeObj.getJSONArray("authors")
                    val publisher = volumeObj.optString("publisher")
                    val publishedDate = volumeObj.optString("publishedDate")
                    val description = volumeObj.optString("description")
                    val pageCount = volumeObj.optInt("pageCount")
                    val imageLinks = volumeObj.optJSONObject("imageLinks")
                    val thumbnail = imageLinks.optString("thumbnail")
                    val previewLink = volumeObj.optString("previewLink")
                    val infoLink = volumeObj.optString("infoLink")
                    val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                    val buyLink = saleInfoObj.optString("buyLink")
                    val authorsArrayList: ArrayList<String> = ArrayList()
                    if (authorsArray.length() != 0) {
                        for (j in 0 until authorsArray.length()) {
                            authorsArrayList.add(authorsArray.optString(i))
                        }
                    }

                    // after extracting all the data we are
                    // saving this data in our modal class.
                    val bookInfo = BookRVModal(
                        title,
                        subtitle,
                        authorsArrayList,
                        publisher,
                        publishedDate,
                        description,
                        pageCount,
                        thumbnail,
                        previewLink,
                        infoLink,
                        buyLink
                    )

                    // below line is use to pass our modal
                    // class in our array list.
                    booksList.add(bookInfo)

                    // below line is use to pass our
                    // array list in adapter class.
                    val adapter = BookRVAdapter(booksList, this@MainActivity)

                    // below line is use to add linear layout
                    // manager for our recycler view.
                    val layoutManager = GridLayoutManager(this, 3)
                    val mRecyclerView = findViewById<RecyclerView>(R.id.idRVBooks) as RecyclerView

                    // in below line we are setting layout manager and
                    // adapter to our recycler view.
                    mRecyclerView.layoutManager = layoutManager
                    mRecyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }, { error ->
            // in this case we are simply displaying a toast message.
            Toast.makeText(this@MainActivity, "No books found..", Toast.LENGTH_SHORT)
                .show()
        })
        // at last we are adding our
        // request to our queue.
        queue.add(request)

    }
}