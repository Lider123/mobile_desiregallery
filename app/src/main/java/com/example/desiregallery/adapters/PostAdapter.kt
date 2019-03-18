package com.example.desiregallery.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.desiregallery.ui.dialogs.ImageRateDialog
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.models.Post
import com.example.desiregallery.ui.activities.FullScreenImageActivity
import com.example.desiregallery.ui.activities.CommentsActivity
import com.example.desiregallery.ui.widgets.SquareImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post.view.*



class PostAdapter(private val items : List<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun getItemCount() : Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : Post = items[position]
        holder.bind(context, item)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val imageView: SquareImageView = view.item_post_image
        private val ratingTextView: TextView = view.item_post_rating
        private val commentView: ImageView = view.item_post_comment
        private val progressBar: ProgressBar = view.item_progress

        private lateinit var rateDialog: ImageRateDialog

        private lateinit var context: Context
        private lateinit var item: Post

        private val handleRate = fun(rate: Float) {
            rateDialog.hide()
            item.updateRating(rate)
            ratingTextView.text = context.getString(R.string.item_post_rating_format, item.getRating())
        }

        fun bind(context: Context, item: Post) {
            this.context = context
            this.item = item

            Picasso.with(context)
                .load(item.getImageUrl().toString())
                .error(R.drawable.image_error)
                .into(imageView, object: Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError() {
                        progressBar.visibility = View.GONE
                    }

                })
            imageView.setOnClickListener {
                val bmpImage = (imageView.drawable as BitmapDrawable).bitmap
                val intent = Intent(context, FullScreenImageActivity::class.java)
                intent.putExtra("bytesImage", Utils.bitmapToBytes(bmpImage))
                context.startActivity(intent)
            }
            ratingTextView.text = context.getString(R.string.item_post_rating_format, item.getRating())
            ratingTextView.text = context.getString(R.string.item_post_rating_format, item.getRating())
            ratingTextView.setOnClickListener{
                rateDialog = ImageRateDialog(context, handleRate)
                rateDialog.show()
            }
            commentView.setOnClickListener{
                val intent = Intent(context, CommentsActivity::class.java)
                intent.putExtra("post", item)
                context.startActivity(intent)
            }
        }
    }
}