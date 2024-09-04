package com.example.readingbooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readingbooks.viewmodels.BookViewModel
import com.example.readinglist.databinding.FragmentBookListBinding

class BookListFragment : Fragment() {

    private var _binding: FragmentBookListBinding? = null
    private val binding get() = _binding!!
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        bookViewModel.allBooks.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookRVAdapter()
        binding.booksRecyclerView.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}