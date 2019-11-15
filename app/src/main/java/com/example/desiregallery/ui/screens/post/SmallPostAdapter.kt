package com.example.desiregallery.ui.screens.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.databinding.ItemPostSmallBinding

/**
 * @author babaetskv on 06.11.19
 */
class SmallPostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<SmallPostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = DataBindingUtil
            .inflate<ItemPostSmallBinding>(inflater, R.layout.item_post_small, parent, false)
        return SmallPostViewHolder(bind)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: SmallPostViewHolder, position: Int) {
        val item = posts[position]
        holder.bind(item)
    }
}