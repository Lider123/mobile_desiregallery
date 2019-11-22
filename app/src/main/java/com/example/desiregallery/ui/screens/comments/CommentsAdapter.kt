package com.example.desiregallery.ui.screens.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.data.models.Comment

class CommentsAdapter : PagedListAdapter<Comment, CommentViewHolder>(Comment.CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = DataBindingUtil.inflate<ItemCommentBinding>(
            inflater,
            R.layout.item_comment,
            parent,
            false
        )
        return CommentViewHolder(bind)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position) as Comment
        holder.bind(item)
    }
}
