package com.example.readingbooks.mybooks
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.readingbooks.R

class BookAdapter(
    // on below line we are passing variables
    // as course list and context
    private var bookList: ArrayList<BookModal>,
    private var ctx: Context,
    private val navigateToDetails: (String) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookAdapter.BookViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_book,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return BookAdapter.BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookViewHolder, position: Int) {
        val bookInfo = bookList[position]
        // below line is use to set image from URL in our image view using Glide.
        Glide.with(ctx)
            .load(bookInfo.thumbnail)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.bookIV)

        holder.bookTitleTV.text = bookInfo.title
        holder.bookAuthorTV.text = "Written By: " + bookInfo.author
        holder.bookPagesTV.text = "Pages: " + bookInfo.pageCount

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener {
            navigateToDetails(bookInfo.id)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our
        // course name text view and our image view.
        val bookTitleTV: TextView = itemView.findViewById(R.id.idTVBookName)
        val bookPagesTV: TextView = itemView.findViewById(R.id.idTVBookPages)
        val bookIV: ImageView = itemView.findViewById(R.id.idIVBook)
        val bookAuthorTV: TextView = itemView.findViewById(R.id.idTVAuthor)
    }
}