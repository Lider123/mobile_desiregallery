package com.example.desiregallery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemPostBinding
import com.example.desiregallery.models.Post
import com.example.desiregallery.presenters.PostPresenter
import com.example.desiregallery.ui.viewholders.PostViewHolder

class PostAdapter(
    private val items: MutableList<Post>
) : RecyclerView.Adapter<PostViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bind =
            DataBindingUtil.inflate<ItemPostBinding>(inflater, R.layout.item_post, parent, false)
        return PostViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = items[position]
        val presenter = PostPresenter(item)
        holder.bind(presenter)
    }

    fun addPosts(posts: List<Post>) {
        val position = items.size
        items.addAll(posts)
        notifyItemRangeInserted(position, posts.size)
    }
}