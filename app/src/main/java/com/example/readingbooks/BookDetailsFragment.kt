package com.example.readingbooks
import android.annotation.SuppressLint
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

class BookDetailsFragment : Fragment() {

    private lateinit var titleTV: TextView
    private lateinit var subtitleTV: TextView
    private lateinit var publisherTV: TextView
    private lateinit var descTV: TextView
    private lateinit var pageTV: TextView
    private lateinit var publisherDateTV: TextView
    private lateinit var previewBtn: Button
    private lateinit var addToBtn: Button
    private lateinit var bookIV: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initializing our variables.
        titleTV = view.findViewById(R.id.idTVTitle)
        subtitleTV = view.findViewById(R.id.idTVSubTitle)
        publisherTV = view.findViewById(R.id.idTVpublisher)
        descTV = view.findViewById(R.id.idTVDescription)
        pageTV = view.findViewById(R.id.idTVNoOfPages)
        publisherDateTV = view.findViewById(R.id.idTVPublishDate)
        previewBtn = view.findViewById(R.id.idBtnPreview)
        addToBtn = view.findViewById(R.id.idBtnAddToList)
        bookIV = view.findViewById(R.id.idIVbook)

        // Retrieve data from arguments
        arguments?.let {
            val title = it.getString("title")
            val subtitle = it.getString("subtitle")
            val publisher = it.getString("publisher")
            val publishedDate = it.getString("publishedDate")
            val description = it.getString("description")
            val pageCount = it.getInt("pageCount")
            val thumbnail = it.getString("thumbnail")
            val previewLink = it.getString("previewLink")
            val infoLink = it.getString("infoLink")
            val buyLink = it.getString("buyLink")

            // Set data to views
            titleTV.text = title
            subtitleTV.text = subtitle
            publisherTV.text = publisher
            publisherDateTV.text = "Published On: $publishedDate"
            descTV.text = description
            pageTV.text = "No Of Pages: $pageCount"
            Glide.with(this).load(thumbnail).into(bookIV)

            previewBtn.setOnClickListener {
                previewLink?.let { link ->
                    if (link.isNotEmpty()) {
                        val uri: Uri = Uri.parse(link)
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } else {
                        Toast.makeText(
                            context,
                            "No preview Link present",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            addToBtn.setOnClickListener {
                buyLink?.let { link ->
                    if (link.isNotEmpty()) {
                        val uri = Uri.parse(link)
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } else {
                        Toast.makeText(
                            context,
                            "No buy page present for this book",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}