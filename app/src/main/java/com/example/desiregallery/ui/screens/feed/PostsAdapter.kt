package com.example.desiregallery.ui.screens.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.databinding.ItemPostBinding

class PostsAdapter : PagedListAdapter<Post, PostViewHolder>(Post.CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = DataBindingUtil
            .inflate<ItemPostBinding>(inflater, R.layout.item_post, parent, false)
        return PostViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position) as Post
        holder.bind(item)
    }
}
