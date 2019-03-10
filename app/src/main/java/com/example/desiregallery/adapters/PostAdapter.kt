package com.example.desiregallery.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.desiregallery.R
import com.example.desiregallery.models.Post
import com.example.desiregallery.ui.widgets.SquareImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post.view.*


class PostAdapter(val items : List<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun getItemCount() : Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : Post = items[position]
        Picasso.with(context)
            .load(item.getImageUrl().toString())
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_error)
            .into(holder.imageView)
        holder.ratingTextView.text = context.getString(R.string.rating_text, item.getRating())

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imageView : SquareImageView = view.item_post_image
        val ratingTextView : TextView = view.item_post_rating
    }
}