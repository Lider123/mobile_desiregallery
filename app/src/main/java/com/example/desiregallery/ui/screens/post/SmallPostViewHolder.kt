package com.example.desiregallery.ui.screens.post

import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.databinding.ItemPostSmallBinding
import com.example.desiregallery.ui.screens.base.BaseViewHolder
import com.squareup.picasso.Picasso
import javax.inject.Inject

/**
 * @author babaetskv on 06.11.19
 */
class SmallPostViewHolder(
    private val bind: ItemPostSmallBinding
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

    override fun updateAuthorName(name: String) = Unit

    override fun updateAuthorPhoto(imageUrl: String) = Unit

    override fun updateTimestamp(time: Long) = Unit

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
        }
    }
}
