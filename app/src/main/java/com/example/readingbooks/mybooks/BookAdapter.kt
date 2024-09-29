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
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookAdapter.BookViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_book,
            parent, false
        )

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
        holder.bookPagesTV.text = "Pages: " + bookInfo.pageCount

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener {
            // inside on click listener method we are calling a new activity
            // and passing all the data of that item in next intent.
            val i = Intent(ctx, BookDetailsActivity::class.java)
            i.putExtra("title", bookInfo.title)
            i.putExtra("subtitle", bookInfo.subtitle)
            i.putExtra("authors", bookInfo.author)
            i.putExtra("publisher", bookInfo.publisher)
            i.putExtra("publishedDate", bookInfo.publishedDate)
            i.putExtra("description", bookInfo.description)
            i.putExtra("pageCount", bookInfo.pageCount)
            i.putExtra("thumbnail", bookInfo.thumbnail)
            i.putExtra("previewLink", bookInfo.previewLink)
            i.putExtra("infoLink", bookInfo.infoLink)

            ctx.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val bookTitleTV: TextView = itemView.findViewById(R.id.idTVBookName)
        val bookPagesTV: TextView = itemView.findViewById(R.id.idTVBookPages)
        val bookIV: ImageView = itemView.findViewById(R.id.idIVBook)
    }
}