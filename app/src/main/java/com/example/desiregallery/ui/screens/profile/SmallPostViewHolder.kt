package com.example.desiregallery.ui.screens.profile

import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemPostSmallBinding
import com.example.desiregallery.ui.presenters.IPostContract
import com.squareup.picasso.Picasso

/**
 * @author babaetskv on 06.11.19
 */
class SmallPostViewHolder(
    private val bind: ItemPostSmallBinding
) : RecyclerView.ViewHolder(bind.root), IPostContract.View {
    private lateinit var presenter: IPostContract.Presenter

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

    override fun updateAuthorName(name: String) {}

    override fun updateAuthorPhoto(imageUrl: String) {}

    override fun updateTimestamp(time: Long) {}

    private fun initListeners() {
        bind.itemPostImage.setOnClickListener {
            presenter.onImageClick(bind.itemPostImage.context)
        }
        bind.itemPostRating.setOnClickListener {
            presenter.onRatingClick(bind.itemPostRating.context)
        }
        bind.itemPostComment.setOnClickListener {
            presenter.onCommentsClick(bind.itemPostComment.context)
        }
    }

    fun bind(presenter: IPostContract.Presenter) {
        this.presenter = presenter
        presenter.attach()
        initListeners()
    }
}