package com.example.desiregallery.ui.screens.feed

import android.content.Intent
import android.view.View
import com.example.desiregallery.R
import com.example.desiregallery.data.models.User
import com.example.desiregallery.ui.screens.base.BaseViewHolder
import com.example.desiregallery.ui.screens.profile.ProfileActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author babaetskv on 09.12.19
 */
class UserViewHolder(itemView: View) : BaseViewHolder<User>(itemView) {
    private val userImageView = itemView.findViewById<CircleImageView>(R.id.item_user_photo)

    override fun bind(item: User) {
        if (item.photo.isNotEmpty()) {
            Picasso.with(itemView.context)
                .load(item.photo)
                .resize(600, 0)
                .placeholder(R.drawable.material)
                .error(R.drawable.image_error)
                .into(userImageView)
        }
        userImageView.setOnClickListener {
            goToProfileActivity(item)
        }
    }

    private fun goToProfileActivity(user: User) {
        val intent = Intent(itemView.context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_USER, user)
        }
        itemView.context.startActivity(intent)
    }
}
