package com.example.desiregallery.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.models.Comment
import com.example.desiregallery.ui.viewholders.CommentViewHolder

class CommentAdapter(private val items: List<Comment>) : RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = DataBindingUtil.inflate<ItemCommentBinding>(inflater, R.layout.item_comment, parent, false)
        return CommentViewHolder(bind)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}