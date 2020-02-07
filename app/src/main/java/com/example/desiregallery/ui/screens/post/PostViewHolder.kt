package com.example.desiregallery.ui.screens.post

import android.view.View
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.databinding.ItemPostBinding
import com.example.desiregallery.ui.screens.base.BaseViewHolder
import com.example.desiregallery.utils.formatDate
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PostViewHolder(
    private val bind: ItemPostBinding
) : BaseViewHolder<Post>(bind.root), IPostContract.View, View.OnClickListener {
    @Inject
    lateinit var presenter: PostPresenter

    override fun bind(item: Post) {
        MainApplication.appComponent.inject(this)
        presenter.attach(this, item)
        with(bind) {
            arrayOf(
                itemPostImage,
                itemPostRating,
                itemPostComment,
                itemAuthorImage
            ).map { it.setOnClickListener(this@PostViewHolder) }
        }
    }

    override fun updateRating(rating: Float) {
        bind.itemPostRating.text = bind.itemPostRating
            .context
            .getString(R.string.item_post_rating_format, rating)
    }

    override fun updateImage(imageUrl: String) = Picasso.with(bind.itemPostImage.context)
        .load(imageUrl)
        .resize(600, 0)
        .placeholder(R.drawable.material)
        .error(R.drawable.image_error)
        .into(bind.itemPostImage)

    override fun updateAuthorName(name: String) {
        bind.itemAuthorText.text = name
    }

    override fun updateAuthorPhoto(imageUrl: String) {
        if (imageUrl.isEmpty()) {
            Picasso.with(bind.itemAuthorImage.context)
                .load(R.drawable.material)
                .resize(100, 100)
                .into(bind.itemAuthorImage)
        } else {
            Picasso.with(bind.itemAuthorImage.context)
                .load(imageUrl)
                .resize(100, 100)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.material)
                .into(bind.itemAuthorImage)
        }
    }

    override fun updateTimestamp(time: Long) {
        bind.itemTimestamp.text = formatDate(bind.itemTimestamp.context, time)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.item_post_image -> presenter.onImageClick(bind.itemPostImage.context)
            R.id.item_post_rating -> presenter.onRatingClick(bind.itemPostRating.context)
            R.id.item_post_comment -> presenter.onCommentsClick(bind.itemPostComment.context)
            R.id.item_author_image -> presenter.onAuthorClick(bind.itemAuthorImage.context)
        }
    }
}
