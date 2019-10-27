package com.example.desiregallery.ui.views

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.desiregallery.R
import com.example.desiregallery.databinding.ItemPostBinding
import com.example.desiregallery.presenters.PostPresenter
import com.example.desiregallery.ui.dialogs.ImageRateDialog
import com.example.desiregallery.utils.formatDate
import com.squareup.picasso.Picasso

class PostViewHolder(
    private val bind: ItemPostBinding
) : RecyclerView.ViewHolder(bind.root), PostView {

    private lateinit var rateDialog: ImageRateDialog
    private lateinit var presenter: PostPresenter

    private val handleRate = fun(rate: Float) {
        rateDialog.hide()
        presenter.updateRating(rate)
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
                .error(R.drawable.image_error)
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

    fun bind(context: Context, presenter: PostPresenter) {
        this.presenter = presenter

        presenter.setImageView()
        presenter.setRating()
        presenter.setAuthorName()
        presenter.setAuthorPhoto()
        presenter.setTimestamp()
        initListeners(context)
    }

    private fun initListeners(context: Context) {
        bind.itemPostImage.setOnClickListener {
            val bmpImage = (bind.itemPostImage.drawable as BitmapDrawable).bitmap
            presenter.goToImageFullScreen(bind.itemPostImage.context, bmpImage)
        }
        bind.itemPostRating.setOnClickListener {
            rateDialog = ImageRateDialog(bind.itemPostRating.context, handleRate)
            rateDialog.show()
        }
        bind.itemPostComment.setOnClickListener {
            presenter.goToCommentActivity(context)
        }
    }
}