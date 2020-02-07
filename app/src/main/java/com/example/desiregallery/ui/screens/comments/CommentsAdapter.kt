package com.example.desiregallery.ui.screens.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.ui.screens.base.BasePagedListAdapter

class CommentsAdapter : BasePagedListAdapter<Comment>(Comment.CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate<ItemCommentBinding>(
            inflater,
            R.layout.item_comment,
            parent,
            false
        ).let {
            CommentViewHolder(it)
        }
    }
}
