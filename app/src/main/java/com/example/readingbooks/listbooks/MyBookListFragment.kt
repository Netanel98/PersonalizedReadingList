package com.example.readingbooks.listbooks

import MyBookListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readingbooks.R
import com.example.readingbooks.databinding.FragmentBookListBinding
import com.example.readingbooks.models.Book

class MyBookListFragment : Fragment() {
    private lateinit var binding: FragmentBookListBinding
    private val bookViewModel: MyBookListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = MyBookListAdapter(bookViewModel::deleteBook)
        binding.booksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        bookViewModel.books.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
        }
    }
}