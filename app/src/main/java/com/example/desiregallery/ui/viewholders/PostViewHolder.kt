package com.example.desiregallery.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemPostBinding
import com.example.desiregallery.ui.contracts.IPostContract
import com.example.desiregallery.utils.formatDate
import com.squareup.picasso.Picasso

class PostViewHolder(
    private val bind: ItemPostBinding
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

    override fun updateAuthorName(name: String) {
        bind.itemAuthorText.text = name
    }

    override fun updateAuthorPhoto(imageUrl: String) {
        if (imageUrl.isEmpty())
            Picasso.with(bind.itemAuthorImage.context)
                .load(R.drawable.material)
                .resize(100, 100)
                .error(R.drawable.image_error)
                .into(bind.itemAuthorImage)
        else
            Picasso.with(bind.itemAuthorImage.context)
                .load(imageUrl)
                .resize(100, 100)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.material)
                .into(bind.itemAuthorImage)
    }

    override fun updateTimestamp(time: Long) {
        bind.itemTimestamp.text = formatDate(bind.itemTimestamp.context, time)
    }

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
        presenter.attach(this)
        initListeners()
    }
}