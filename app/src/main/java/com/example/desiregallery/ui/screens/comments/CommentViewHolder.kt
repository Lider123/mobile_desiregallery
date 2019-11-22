package com.example.desiregallery.ui.screens.comments

import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.utils.formatDate
import com.squareup.picasso.Picasso

/**
 * @author babaetskv on 24.10.19
 */
class CommentViewHolder(private val bind: ItemCommentBinding) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Comment) {
        with(bind) {
            itemCommentText.text = item.text
            itemCommentTime.text = formatDate(itemCommentTime.context, item.timestamp)
            itemAuthorName.text = item.author.login
            if (item.author.photo.isEmpty()) {
                Picasso.with(itemAuthorPhoto.context)
                    .load(R.drawable.material)
                    .resize(100, 100)
                    .error(R.drawable.image_error)
                    .into(itemAuthorPhoto)
            } else {
                Picasso.with(itemAuthorPhoto.context)
                    .load(item.author.photo)
                    .resize(100, 100)
                    .error(R.drawable.image_error)
                    .placeholder(R.drawable.material)
                    .into(itemAuthorPhoto)
            }
        }
    }
}