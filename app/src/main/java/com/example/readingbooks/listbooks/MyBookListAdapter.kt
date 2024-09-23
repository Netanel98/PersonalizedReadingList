import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.readingbooks.databinding.ItemBookBinding
import com.example.readingbooks.listbooks.MyBookListViewModel
import com.example.readingbooks.models.Book

class MyBookListAdapter(private val deleteCallback: DeleteCallback) : ListAdapter<Book, MyBookListAdapter.BookViewHolder>(BookDiffCallback()) {

    interface DeleteCallback {
        fun onDelete(book: Book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            deleteCallback
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(
        private val binding: ItemBookBinding,
        private val deleteCallback: DeleteCallback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.book = book
            binding.deleteCallback = deleteCallback
            binding.executePendingBindings()
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