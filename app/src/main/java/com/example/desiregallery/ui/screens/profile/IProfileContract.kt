package com.example.desiregallery.ui.screens.profile

import android.graphics.Bitmap
import androidx.fragment.app.FragmentManager
import com.example.desiregallery.data.models.Post

/**
 * @author babaetskv on 29.10.19
 */
interface IProfileContract {
    interface View {
        fun updateName(title: String)
        fun updateAge(age: Int?)
        fun updatePostsCount(count: Int?)
        fun updateAverageRating(rating: Float?)
        fun updatePhoto(photoUrl: String)
        fun updatePhoto(bitmap: Bitmap)
        fun updatePosts(posts: List<Post>)
        fun updateEditButtonVisibility(visible: Boolean)
        fun showMessage(message: String)
    }

    interface Presenter {
        fun attach(view: View)
        fun onEditClick(fragmentManager: FragmentManager)
    }
}
