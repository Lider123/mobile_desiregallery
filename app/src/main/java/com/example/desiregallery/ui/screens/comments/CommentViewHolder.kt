package com.example.desiregallery.ui.screens.comments

import android.content.Context
import android.content.Intent
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemCommentBinding
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.User
import com.example.desiregallery.ui.screens.base.BaseViewHolder
import com.example.desiregallery.ui.screens.profile.ProfileActivity
import com.example.desiregallery.utils.formatDate
import com.squareup.picasso.Picasso

/**
 * @author babaetskv on 24.10.19
 */
class CommentViewHolder(private val bind: ItemCommentBinding) : BaseViewHolder<Comment>(bind.root) {

    override fun bind(item: Comment) {
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
            itemAuthorPhoto.setOnClickListener {
                goToProfileActivity(root.context, item.author)
            }
        }
    }

    private fun goToProfileActivity(context: Context, author: User) {
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_USER, author)
        }
        context.startActivity(intent)
    }
}
