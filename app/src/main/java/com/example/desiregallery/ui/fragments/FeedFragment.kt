package com.example.desiregallery.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.listeners.PaginationListener
import com.example.desiregallery.models.Post
import com.example.desiregallery.ui.dialogs.PostCreationDialog
import com.example.desiregallery.viewmodels.PostListViewModel
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FeedFragment : androidx.fragment.app.Fragment() {
    private lateinit var model: PostListViewModel
    private val mPostAdapter = PostAdapter(mutableListOf())

    private val addPost = fun(post: Post) {
        model.addPost(post)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        view.feed_fab.setOnClickListener { CropImage.activity().start(context!!, this) }

        with(view.post_list) {
            setItemViewCacheSize(20)
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            adapter = mPostAdapter
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            addOnScrollListener(object: PaginationListener(lm) {
                override val pageSize: Int
                    get() = model.pageSize

                override fun loadMoreItems() = model.loadPosts()

                override fun isLastPage() = model.isLastPage

                override fun isLoading() = model.isLoading.value as Boolean
            })
        }

        initModel()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getActivityResult(data).uri
            val istream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(istream)
            PostCreationDialog(activity!!, selectedImage, addPost).show()
        }
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(PostListViewModel::class.java)
        model.posts.observe(this, Observer<List<Post>> { posts ->
            mPostAdapter.addPosts(posts)
        })
        model.isLoading.observe(this, Observer<Boolean> { isLoading ->
            feed_progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }
}
