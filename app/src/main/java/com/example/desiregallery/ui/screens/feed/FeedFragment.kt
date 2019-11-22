package com.example.desiregallery.ui.screens.feed

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.ui.screens.post.PostAdapter
import com.example.desiregallery.ui.screens.postcreation.IPostCreationListener
import com.example.desiregallery.ui.screens.postcreation.PostCreationDialog
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import javax.inject.Inject

class FeedFragment : Fragment() {
    @Inject
    lateinit var vmFactory: PostsViewModel.Factory

    private lateinit var model: PostsViewModel
    private lateinit var snackbar: SnackbarWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        initModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.post_list) {
            setItemViewCacheSize(20)
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        }
        snackbar = SnackbarWrapper(feed_container)
        feed_fab.setOnClickListener { CropImage.activity().start(context!!, this) }
        swipe_container.setOnRefreshListener {
            model.updatePosts()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getActivityResult(data).uri
            val istream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(istream)
            PostCreationDialog(
                activity!!,
                selectedImage,
                object : IPostCreationListener {

                    override fun onSubmit(post: Post) {
                        model.addPost(post)
                        snackbar.show(getString(R.string.post_publish_success))
                    }

                    override fun onCancel() {
                    }
                }).show()
        }
    }

    private fun initModel() {
        model = ViewModelProviders.of(this, vmFactory).get(PostsViewModel::class.java)
        model.postsLiveData.observe(this, Observer { posts ->
            val adapter = PostAdapter()
            adapter.submitList(posts)
            post_list.adapter = adapter
        })
        model.getState().observe(this, Observer { status ->
            status ?: return@Observer

            when (status) {
                RequestState.DOWNLOADING -> {
                    showLoading()
                    updateHintVisibility(false)
                }
                RequestState.SUCCESS -> {
                    hideLoading()
                    updateHintVisibility(false)
                }
                RequestState.ERROR_DOWNLOAD -> {
                    hideLoading()
                    updateHintVisibility(false)
                    snackbar.show(resources.getString(R.string.posts_download_error))
                }
                RequestState.ERROR_UPLOAD -> {
                    hideLoading()
                    updateHintVisibility(false)
                    snackbar.show(resources.getString(R.string.post_upload_error))
                }
                RequestState.NO_DATA -> {
                    hideLoading()
                    updateHintVisibility(true)
                }
            }
        })
    }

    private fun showLoading() {
        swipe_container.isRefreshing = true
    }

    private fun hideLoading() {
        swipe_container.isRefreshing = false
    }

    private fun updateHintVisibility(visible: Boolean) {
        feed_hint.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
