package com.example.desiregallery.adapters

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.desiregallery.ui.dialogs.ImageRateDialog
import com.example.desiregallery.R
import com.example.desiregallery.models.Post
import com.example.desiregallery.presenters.PostPresenter
import com.example.desiregallery.ui.views.PostView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(
    private val items: List<Post>,
    private val context: Context
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val presenter = PostPresenter(holder, item)
        holder.bind(context, presenter)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), PostView {
        private val imageView: ImageView = view.item_post_image
        private val ratingTextView: TextView = view.item_post_rating
        private val commentView: ImageView = view.item_post_comment
        private val progressBar: ProgressBar = view.item_progress
        private val authorTextView: TextView = view.item_author_text
        private val authorImage: CircleImageView = view.item_author_image

        private lateinit var rateDialog: ImageRateDialog

        private lateinit var context: Context
        private lateinit var presenter: PostPresenter

        private val handleRate = fun(rate: Float) {
            rateDialog.hide()
            presenter.updateRating(rate)
        }

        override fun updateRating(rating: Float) {
            ratingTextView.text = context.getString(R.string.item_post_rating_format, rating)
        }

        override fun updateImage(imageUrl: String) {
            Picasso.with(context)
                .load(imageUrl)
                .error(R.drawable.image_error)
                .into(imageView, object: Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError() {
                        progressBar.visibility = View.GONE
                    }
                })
        }

        override fun updateAuthorName(name: String) {
            authorTextView.text = name
        }

        override fun updateAuthorPhoto(imageUrl: String) {
            if (imageUrl.isEmpty()) {
                Picasso.with(context)
                    .load(R.drawable.material)
                    .error(R.drawable.image_error)
                    .into(authorImage)
            }
            else {
                Picasso.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image_error)
                    .placeholder(R.drawable.material)
                    .into(authorImage)
            }
        }

        fun bind(context: Context, presenter: PostPresenter) {
            this.context = context
            this.presenter = presenter

            presenter.setImageView()
            presenter.setRating()
            presenter.setAuthorName()
            presenter.setAuthorPhoto()
            initListeners()
        }

        private fun initListeners() {
            imageView.setOnClickListener {
                val bmpImage = (imageView.drawable as BitmapDrawable).bitmap
                presenter.goToImageFullScreen(context, bmpImage)
            }
            ratingTextView.setOnClickListener{
                rateDialog = ImageRateDialog(context, handleRate)
                rateDialog.show()
            }
            commentView.setOnClickListener{
                presenter.goToCommentActivity(context)
            }
        }
    }
}