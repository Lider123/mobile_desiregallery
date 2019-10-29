package com.example.desiregallery.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.models.Comment
import com.example.desiregallery.utils.formatDate
import com.squareup.picasso.Picasso

/**
 * @author babaetskv on 24.10.19
 */
class CommentViewHolder(private val bind: ItemCommentBinding) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Comment) {
        bind.itemCommentText.text = item.text
        bind.itemCommentTime.text = formatDate(bind.itemCommentTime.context, item.datetime)
        bind.itemAuthorName.text = item.author.login
        if (item.author.photo.isEmpty())
            Picasso.with(bind.itemAuthorPhoto.context)
                .load(R.drawable.material)
                .resize(100, 100)
                .error(R.drawable.image_error)
                .into(bind.itemAuthorPhoto)
        else
            Picasso.with(bind.itemAuthorPhoto.context)
                .load(item.author.photo)
                .resize(100, 100)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.material)
                .into(bind.itemAuthorPhoto)
    }
}