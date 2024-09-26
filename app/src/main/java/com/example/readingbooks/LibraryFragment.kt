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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.readingbooks.databinding.FragmentLibraryBinding
import com.example.readingbooks.mybooks.BookAdapter
import com.example.readingbooks.mybooks.BookModal

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var booksList: ArrayList<BookModal>
    private lateinit var loadingPB: ProgressBar
    private lateinit var searchEdt: EditText
    private lateinit var searchBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        // Initializing our variables with their IDs.
        loadingPB = view.findViewById(R.id.idLoadingPB)
        searchEdt = view.findViewById(R.id.idEdtSearchBooks)
        searchBtn = view.findViewById(R.id.idBtnSearch)

        // Adding click listener for search button
        searchBtn.setOnClickListener {
            loadingPB.visibility = View.VISIBLE
            // Checking if our EditText field is empty.
            if (searchEdt.text.toString().isEmpty()) {
                searchEdt.error = "Please enter search query"
            } else {
                // If the search query is not empty, we call the method to load books from the API.
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

                    val id = volumeObj.optString("id", "N/A")
                    val title = volumeObj.optString("title", "N/A")
                    val subtitle = volumeObj.optString("subtitle", "N/A")
                    val authors = volumeObj.optString("authors", "N/A")
                    val publisher = volumeObj.optString("publisher", "N/A")
                    val publishedDate = volumeObj.optString("publishedDate", "N/A")
                    val description = volumeObj.optString("description", "N/A")
                    val pageCount = volumeObj.optInt("pageCount", 0)  // Default to 0 if not present

                    val imageLinks = volumeObj.optJSONObject("imageLinks")
                    val thumbnail = imageLinks?.optString("thumbnail", "N/A") ?: "N/A"  // Default thumbnail if not present

                    val previewLink = volumeObj.optString("previewLink", "N/A")
                    val infoLink = volumeObj.optString("infoLink", "N/A")

                    val saleInfoObj = itemsObj.optJSONObject("saleInfo")

                    val bookInfo = BookModal(id, title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink)
                    booksList.add(bookInfo)
                }
                setupRecyclerView()
            } catch (e: Exception) {
                Toast.makeText(context, "Error parsing book data.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }, { error ->
            Toast.makeText(context, "No books found..", Toast.LENGTH_SHORT).show()
        })
        mRequestQueue.add(request)
    }

    private fun setupRecyclerView() {
        val adapter = BookAdapter(ArrayList(), book = BookModal("", "", "", "", "",
            "", "", 0, "",
            "", ""), ctx = requireContext()) { book ->
            navigateToBookDetails(book)
        }
        binding.idRVBooks.layoutManager = GridLayoutManager(context, 3)
        binding.idRVBooks.adapter = adapter
    }

    private fun navigateToBookDetails(book: BookModal) {
        val action = LibraryFragmentDirections.actionLibraryFragmentToBookDetailsFragment()
        findNavController().navigate(action)
    }
}