package com.example.desiregallery.ui.screens.post

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
) : BaseViewHolder<Post>(bind.root), IPostContract.View {
    @Inject
    lateinit var presenter: PostPresenter

    override fun bind(item: Post) {
        MainApplication.appComponent.inject(this)
        presenter.attach(this, item)
        initListeners()
    }

    override fun updateRating(rating: Float) {
        bind.itemPostRating.text = bind.itemPostRating
            .context
            .getString(R.string.item_post_rating_format, rating)
    }

    override fun updateImage(imageUrl: String) {
        Picasso.with(bind.itemPostImage.context)
            .load(imageUrl)
            .resize(600, 0)
            .placeholder(R.drawable.material)
            .error(R.drawable.image_error)
            .into(bind.itemPostImage)
    }

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

    private fun initListeners() {
        with(bind) {
            itemPostImage.setOnClickListener {
                presenter.onImageClick(bind.itemPostImage.context)
            }
            itemPostRating.setOnClickListener {
                presenter.onRatingClick(bind.itemPostRating.context)
            }
            itemPostComment.setOnClickListener {
                presenter.onCommentsClick(bind.itemPostComment.context)
            }
            itemAuthorImage.setOnClickListener {
                presenter.onAuthorClick(bind.itemAuthorImage.context)
            }
        }
    }
}
