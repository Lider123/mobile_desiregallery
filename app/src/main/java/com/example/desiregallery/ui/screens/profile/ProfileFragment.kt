package com.example.desiregallery.ui.screens.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : Fragment(), IProfileContract.View {
    @Inject
    lateinit var presenter: ProfilePresenter

    private lateinit var snackbar: SnackbarWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = SnackbarWrapper(profile_content)
        presenter.attach(this)
        initListeners()
    }

    override fun updateName(title: String) {
        profile_name.text = title
    }

    override fun updatePhoto(photoUrl: String) {
        Picasso.with(activity)
            .load(photoUrl)
            .into(profile_photo)
    }

    override fun updatePhoto(bitmap: Bitmap) {
        profile_photo.setImageBitmap(bitmap)
    }

    override fun updateAge(age: Int?) {
        profile_age.text = getString(R.string.profile_age_template, age)
    }

    override fun updatePostsCount(count: Int?) {
        profile_posts_count.text = getString(R.string.profile_posts_template, count)
    }

    override fun updateAverageRating(rating: Float?) {
        profile_average_rating.text = getString(R.string.profile_rating_template, rating)
    }

    override fun updateEditButtonVisibility(visible: Boolean) {
        profile_edit.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun updatePosts(posts: List<Post>) {
        val lm = GridLayoutManager(context, 2)
        val adapter = SmallPostAdapter(posts)
        profile_posts.layoutManager = lm
        profile_posts.adapter = adapter
    }

    override fun showMessage(message: String) {
        snackbar.show(message)
    }

    private fun initListeners() {
        profile_edit.setOnClickListener { presenter.onEditClick(requireFragmentManager()) }
    }
}
