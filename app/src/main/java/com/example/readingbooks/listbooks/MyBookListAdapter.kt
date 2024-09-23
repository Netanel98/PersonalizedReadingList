import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.readingbooks.databinding.ItemBookBinding
import com.example.readingbooks.models.Book

class MyBookListAdapter(private val onDelete: (Book) -> Unit) : ListAdapter<Book, MyBookListAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDelete  // Passing the lambda directly to the ViewHolder
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(
        private val binding: ItemBookBinding,
        private val onDelete: (Book) -> Unit  // Using the lambda function directly
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.book = book
            binding.executePendingBindings()

            // Set up the delete button click listener to use the lambda function
            binding.btnDelete.setOnClickListener {
                onDelete(book)  // Invoking the lambda function directly
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}