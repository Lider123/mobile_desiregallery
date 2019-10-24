package com.example.desiregallery.ui.views

import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.Utils
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.models.Comment

/**
 * @author babaetskv on 24.10.19
 */
class CommentViewHolder(private val bind: ItemCommentBinding) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Comment) {
        bind.itemCommentText.text = item.text
        bind.itemCommentTime.text = Utils.formatDate(bind.itemCommentTime.context, item.datetime)
    }
}