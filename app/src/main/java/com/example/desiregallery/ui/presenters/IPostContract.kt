package com.example.desiregallery.ui.presenters

import android.content.Context
import com.example.desiregallery.data.models.Post

/**
 * @author babaetskv on 29.10.19
 */
interface IPostContract {
    interface View {
        fun updateRating(rating: Float)
        fun updateImage(imageUrl: String)
        fun updateAuthorName(name: String)
        fun updateAuthorPhoto(imageUrl: String)
        fun updateTimestamp(time: Long)
    }

    interface Presenter {
        fun attach(view: View, post: Post)
        fun onImageClick(context: Context)
        fun onRatingClick(context: Context)
        fun onCommentsClick(context: Context)
    }
}