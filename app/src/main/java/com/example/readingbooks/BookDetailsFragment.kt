package com.example.readingbooks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class BookDetailsFragment : Fragment() {

    private lateinit var titleTV: TextView
    private lateinit var subtitleTV: TextView
    private lateinit var publisherTV: TextView
    private lateinit var descTV: TextView
    private lateinit var pageTV: TextView
    private lateinit var publisherDateTV: TextView
    private lateinit var previewBtn: Button
    private lateinit var buyBtn: Button
    private lateinit var bookIV: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTV = view.findViewById(R.id.idTVTitle)
        subtitleTV = view.findViewById(R.id.idTVSubTitle)
        publisherTV = view.findViewById(R.id.idTVpublisher)
        descTV = view.findViewById(R.id.idTVDescription)
        pageTV = view.findViewById(R.id.idTVNoOfPages)
        publisherDateTV = view.findViewById(R.id.idTVPublishDate)
        previewBtn = view.findViewById(R.id.idBtnPreview)
        buyBtn = view.findViewById(R.id.idBtnBuy)
        bookIV = view.findViewById(R.id.idIVbook)

        arguments?.let {
            val title = it.getString("title")
            val subtitle = it.getString("subtitle")
            val publisher = it.getString("publisher")
            val publishedDate = it.getString("publishedDate")
            val description = it.getString("description")
            val pageCount = it.getInt("pageCount", 0)
            val thumbnail = it.getString("thumbnail")
            val previewLink = it.getString("previewLink")
            val infoLink = it.getString("infoLink")
            val buyLink = it.getString("buyLink")

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
                    Toast.makeText(context, "No preview Link present", Toast.LENGTH_SHORT).show()
                } else {
                    val uri: Uri = Uri.parse(previewLink)
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            }

            buyBtn.setOnClickListener {
                if (buyLink.isNullOrEmpty()) {
                    Toast.makeText(context, "No buy page present for this book", Toast.LENGTH_SHORT).show()
                } else {
                    val uri = Uri.parse(buyLink)
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            }
        }
    }
}