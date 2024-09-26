package com.example.readingbooks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.readingbooks.listbooks.MyBookListViewModel

class BookDetailsActivity : AppCompatActivity() {

    lateinit var titleTV: TextView
    lateinit var subtitleTV: TextView
    lateinit var publisherTV: TextView
    lateinit var descTV: TextView
    lateinit var pageTV: TextView
    lateinit var publisherDateTV: TextView
    lateinit var previewBtn: Button
    //lateinit var saveBtn: Button
    lateinit var buyBtn: Button
    lateinit var bookIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        titleTV = findViewById(R.id.idTVTitle)
        subtitleTV = findViewById(R.id.idTVSubTitle)
        publisherTV = findViewById(R.id.idTVpublisher)
        descTV = findViewById(R.id.idTVDescription)
        pageTV = findViewById(R.id.idTVNoOfPages)
        publisherDateTV = findViewById(R.id.idTVPublishDate)
        previewBtn = findViewById(R.id.idBtnPreview)
        //saveBtn = findViewById(R.id.idBtnSave)
        buyBtn = findViewById(R.id.idBtnBuy)
        bookIV = findViewById(R.id.idIVbook)

        val id = intent.getStringExtra("id").toString()
        val title = intent.getStringExtra("title").toString()
        val subtitle = intent.getStringExtra("subtitle").toString()
        val author = intent.getStringExtra("author").toString()
        val publisher = intent.getStringExtra("publisher").toString()
        val publishedDate = intent.getStringExtra("publishedDate").toString()
        val description = intent.getStringExtra("description").toString()
        val pageCount = intent.getIntExtra("pageCount", 0)
        val thumbnail = intent.getStringExtra("thumbnail").toString()
        val previewLink = intent.getStringExtra("previewLink").toString()
        val infoLink = intent.getStringExtra("infoLink").toString()
        val buyLink = intent.getStringExtra("buyLink").toString()

        titleTV.text = title
        subtitleTV.text = subtitle
        publisherTV.text = publisher
        publisherDateTV.text = "Published On : $publishedDate"
        descTV.text = description
        pageTV.text = "No Of Pages : $pageCount"

        Glide.with(this)
            .load(thumbnail)
            .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.error))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(bookIV)

        previewBtn.setOnClickListener {
            if (previewLink.isNullOrEmpty()) {
                Toast.makeText(this, "No preview Link present", Toast.LENGTH_SHORT).show()
            } else {
                val uri: Uri = Uri.parse(previewLink)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        buyBtn.setOnClickListener {
            if (buyLink.isNullOrEmpty()) {
                Toast.makeText(this, "No buy page present for this book", Toast.LENGTH_SHORT).show()
            } else {
                val uri = Uri.parse(buyLink)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }
}