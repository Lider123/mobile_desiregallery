package com.example.desiregallery.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemPostBinding
import com.example.desiregallery.data.models.Post

class PostAdapter : PagedListAdapter<Post, PostViewHolder>(Post.CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = DataBindingUtil
            .inflate<ItemPostBinding>(inflater, R.layout.item_post, parent, false)
        return PostViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position) as Post
        val presenter = PostPresenter(item)
        holder.bind(presenter)
    }
}
