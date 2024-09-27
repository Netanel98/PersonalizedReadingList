package com.example.readingbooks.mybooks
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.readingbooks.R

class BookDetailsActivity : AppCompatActivity() {

    // creating variables for strings, text view,
    // image views, and buttons.
    lateinit var titleTV: TextView
    lateinit var subtitleTV: TextView
    lateinit var publisherTV: TextView
    lateinit var descTV: TextView
    lateinit var pageTV: TextView
    lateinit var publisherDateTV: TextView
    lateinit var previewBtn: Button
    lateinit var buyBtn: Button
    lateinit var bookIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        // initializing our variables.
        titleTV = findViewById(R.id.idTVTitle)
        subtitleTV = findViewById(R.id.idTVSubTitle)
        publisherTV = findViewById(R.id.idTVpublisher)
        descTV = findViewById(R.id.idTVDescription)
        pageTV = findViewById(R.id.idTVNoOfPages)
        publisherDateTV = findViewById(R.id.idTVPublishDate)
        previewBtn = findViewById(R.id.idBtnPreview)
        buyBtn = findViewById(R.id.idBtnBuy)
        bookIV = findViewById(R.id.idIVbook)

        // getting the data which we have passed from our adapter class.
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val publisher = intent.getStringExtra("publisher")
        val publishedDate = intent.getStringExtra("publishedDate")
        val description = intent.getStringExtra("description")
        val pageCount = intent.getIntExtra("pageCount", 0)
        val thumbnail = intent.getStringExtra("thumbnail")
        val previewLink = intent.getStringExtra("previewLink")
        val infoLink = intent.getStringExtra("infoLink")
        val buyLink = intent.getStringExtra("buyLink")

        // setting the data to our text views and image view.
        titleTV.text = title
        subtitleTV.text = subtitle
        publisherTV.text = publisher
        publisherDateTV.text = "Published On : $publishedDate"
        descTV.text = description
        pageTV.text = "No Of Pages : $pageCount"

        // Loading image using Glide
        Glide.with(this)
            .load(thumbnail)
            .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.error))
            .into(bookIV)

        // adding on click listener for our preview button.
        previewBtn.setOnClickListener {
            if (previewLink.isNullOrEmpty()) {
                Toast.makeText(this, "No preview Link present", Toast.LENGTH_SHORT).show()
            } else {
                val uri: Uri = Uri.parse(previewLink)
                val i = Intent(Intent.ACTION_VIEW, uri)
                startActivity(i)
            }
        }

        // adding click listener for buy button
        buyBtn.setOnClickListener {
            if (buyLink.isNullOrEmpty()) {
                Toast.makeText(this, "No buy page present for this book", Toast.LENGTH_SHORT).show()
            } else {
                val uri = Uri.parse(buyLink)
                val i = Intent(Intent.ACTION_VIEW, uri)
                startActivity(i)
            }
        }
    }
}