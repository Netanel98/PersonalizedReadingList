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
    private var booksList: ArrayList<BookModal>,
    private var book: BookModal = BookModal("", "", "", "", "",
        "", "", 0 , "", "", ""),
    private var ctx: Context,
    private var itemClickListener: (BookModal) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_book,
            parent, false
        )
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: _root_ide_package_.com.example.readingbooks.mybooks.BookAdapter.BookViewHolder, position: Int) {
        val bookInfo = booksList[position]
        holder.bind(book, ctx)
        holder.itemView.setOnClickListener { itemClickListener(book) }
        // below line is use to set image from URL in our image view using Glide.
        Glide.with(ctx)
            .load(bookInfo.thumbnail)
            .apply(RequestOptions().placeholder(_root_ide_package_.com.example.readingbooks.R.drawable.placeholder).error(
                _root_ide_package_.com.example.readingbooks.R.drawable.error
            ))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.bookIV)

        holder.bookTitleTV.text = bookInfo.title
        holder.bookPagesTV.text = "Pages: " + bookInfo.pageCount

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener {
            // inside on click listener method we are calling a new activity
            // and passing all the data of that item in next intent.
            val i = Intent(ctx, BookDetailsFragment::class.java)
            i.putExtra("title", bookInfo.title)
            i.putExtra("subtitle", bookInfo.subtitle)
            i.putExtra("author", bookInfo.author)
            i.putExtra("publisher", bookInfo.publisher)
            i.putExtra("publishedDate", bookInfo.publishedDate)
            i.putExtra("description", bookInfo.description)
            i.putExtra("pageCount", bookInfo.pageCount)
            i.putExtra("thumbnail", bookInfo.thumbnail)
            i.putExtra("previewLink", bookInfo.previewLink)
            i.putExtra("infoLink", bookInfo.infoLink)
            // after passing that data we are
            // starting our new intent.
            ctx.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitleTV: TextView = itemView.findViewById(R.id.idTVBookName)
        val bookPagesTV: TextView = itemView.findViewById(R.id.idTVBookPages)
        val bookIV: ImageView = itemView.findViewById(R.id.idIVBook)

        fun bind(book: BookModal, context: Context){
            bookTitleTV.text = book.title
            bookPagesTV.text = "Pages: " + book.pageCount
            Glide.with(context)
                .load(book.thumbnail)
        }
    }
}