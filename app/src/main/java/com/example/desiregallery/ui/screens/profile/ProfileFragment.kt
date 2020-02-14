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
import com.example.desiregallery.auth.IAccount
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.ui.IOnBackPressed
import com.example.desiregallery.ui.screens.post.SmallPostAdapter
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment private constructor(private val account: IAccount?) : Fragment(),
    IProfileContract.View, IOnBackPressed {
    @Inject
    lateinit var presenter: ProfilePresenter

    private lateinit var snackbar: SnackbarWrapper
    private var editFragment: EditProfileFragment? = null
    private var editFragmentIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.instance.plusProfileComponent(account).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = SnackbarWrapper(profile_content)
        presenter.attach(this)
        profile_edit.setOnClickListener { presenter.onEditClick() }
    }

    override fun onStop() {
        super.onStop()
        presenter.detach()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainApplication.instance.clearProfileComponent()
    }

    override fun updateName(title: String) {
        profile_name.text = title
    }

    override fun updatePhoto(photoUrl: String) {
        Picasso.with(activity)
            .load(photoUrl)
            .into(profile_photo)
    }

    override fun updatePhoto(bitmap: Bitmap) = profile_photo.setImageBitmap(bitmap)

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
        profile_posts.layoutManager = GridLayoutManager(context, 2)
        profile_posts.adapter = SmallPostAdapter(posts)
    }

    override fun showMessage(message: String) = snackbar.show(message)

    override fun updateNoPostsHintVisibility(visible: Boolean) {
        hint_no_posts.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun showEditFragment(fragment: EditProfileFragment) {
        editFragment = fragment
        val modalFragment = ModalFragmentFactory(requireFragmentManager()).create(
            editFragment as EditProfileFragment,
            R.id.profile_container
        )
        requireFragmentManager().beginTransaction()
            .add(R.id.profile_container, modalFragment)
            .commit()
        editFragmentIsShown = true
    }

    override fun hideEditFragment() {
        editFragment?.let {
            requireFragmentManager().beginTransaction()
                .remove(it)
                .commit()
            editFragmentIsShown = false
        }
    }

    override fun onBackPressed() = if (editFragmentIsShown) {
        hideEditFragment()
        true
    } else false

    companion object {

        fun createInstance(account: IAccount) = ProfileFragment(account)
    }
}
