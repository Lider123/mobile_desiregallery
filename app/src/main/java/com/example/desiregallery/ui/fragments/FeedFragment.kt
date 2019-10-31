package com.example.desiregallery.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.listeners.PaginationListener
import com.example.desiregallery.models.Post
import com.example.desiregallery.storage.IStorageHelper
import com.example.desiregallery.ui.dialogs.PostCreationDialog
import com.example.desiregallery.viewmodels.PostListViewModel
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FeedFragment : Fragment() {
    private val model: PostListViewModel by viewModel()
    private val mPostAdapter: PostAdapter by inject { parametersOf(mutableListOf<Post>()) }
    private val storageHelper: IStorageHelper by inject()
    private val accProvider: AccountProvider by inject()

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

        setObservers()
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getActivityResult(data).uri
            val istream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(istream)
            PostCreationDialog(activity!!, selectedImage, accProvider, storageHelper, addPost).show()
        }
    }

    private fun setObservers() {
        model.posts.observe(this, Observer<List<Post>> { posts ->
            mPostAdapter.addPosts(posts)
        })
        model.isLoading.observe(this, Observer<Boolean> { isLoading ->
            feed_progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }
}
