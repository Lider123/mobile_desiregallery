package com.example.desiregallery.ui.screens.post

import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.databinding.ItemPostSmallBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject

/**
 * @author babaetskv on 06.11.19
 */
class SmallPostViewHolder(
    private val bind: ItemPostSmallBinding
) : RecyclerView.ViewHolder(bind.root), IPostContract.View {
    @Inject
    lateinit var presenter: PostPresenter

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
        with (bind) {
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

    fun bind(post: Post) {
        MainApplication.appComponent.inject(this)
        presenter.attach(this, post)
        initListeners()
    }
}