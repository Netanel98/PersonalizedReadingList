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

class BookListFragment : Fragment() {

    private var _binding: FragmentBookListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyBookListViewModel by viewModels()
    private lateinit var bookAdapter: MyBookListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.books.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = MyBookListAdapter(object : MyBookListAdapter.DeleteCallback {
            override fun onDelete(book: Book) {
                viewModel.deleteBook(book)
            }
        })
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