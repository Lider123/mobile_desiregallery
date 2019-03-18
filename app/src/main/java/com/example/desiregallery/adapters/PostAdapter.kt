package com.example.desiregallery.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.models.Post
import com.example.desiregallery.ui.activities.FullScreenImageActivity
import com.example.desiregallery.ui.activities.CommentsActivity
import com.example.desiregallery.ui.widgets.SquareImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post.view.*
import java.io.ByteArrayOutputStream
import java.nio.channels.InterruptedByTimeoutException


class PostAdapter(val items : List<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun getItemCount() : Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : Post = items[position]
        holder.bind(context, item)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val imageView: SquareImageView = view.item_post_image
        private val ratingTextView: TextView = view.item_post_rating
        private val commentView: ImageView = view.item_post_comment
        private val progressBar: ProgressBar = view.item_progress

        fun bind(context: Context, item: Post) {
            Picasso.with(context)
                .load(item.getImageUrl().toString())
                .error(R.drawable.image_error)
                .into(imageView, object: Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError() {
                        progressBar.visibility = View.GONE
                    }

                })
            imageView.setOnClickListener {
                val bmpImage = (imageView.drawable as BitmapDrawable).bitmap
                val intent = Intent(context, FullScreenImageActivity::class.java)
                intent.putExtra("bytesImage", Utils.bitmapToBytes(bmpImage))
                context.startActivity(intent)
            }
            ratingTextView.text = context.getString(R.string.rating_text_format, item.getRating())
            ratingTextView.setOnClickListener{
                // TODO: handle onClick event for the rating view
                Toast.makeText(context, "Rating view has been pressed for image " + item.getId(), Toast.LENGTH_SHORT).show()
            }
            commentView.setOnClickListener{
                // TODO: handle onClick event for the comment view
                val intent = Intent(context, CommentsActivity::class.java)
                intent.putExtra("post", item)
                context.startActivity(intent)
            }
        }
    }
}