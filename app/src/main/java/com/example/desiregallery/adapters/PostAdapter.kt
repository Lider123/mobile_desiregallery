package com.example.desiregallery.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.models.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post.view.*


class PostAdapter(val items : List<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun getItemCount() : Int {
        return items.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item : Post = items.get(p1)
        Picasso.with(context)
            .load(item.getImageUrl().toString())
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(p0.imageView)
        p0.ratingTextView.text = item.getRating().toString()

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imageView = view.item_post_image
        val ratingTextView = view.item_post_rating
    }
}