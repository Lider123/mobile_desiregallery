package com.example.desiregallery.ui.screens.comments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.toolbar_comments.*
import javax.inject.Inject

class CommentsActivity : AppCompatActivity() {
    @Inject
    lateinit var accProvider: AccountProvider
    @Inject
    lateinit var vmFactory: CommentsViewModel.Factory

    private lateinit var snackbar: SnackbarWrapper
    private lateinit var post: Post
    private lateinit var model: CommentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        comments_button_back.setOnClickListener {
            onBackPressed()
        }
        comments_button_send.setOnClickListener {
            val commentText = comments_input.text.trim()
            if (commentText.isNotEmpty()) handleSend(commentText.toString())
        }
        comments_list.layoutManager = LinearLayoutManager(this)
        snackbar = SnackbarWrapper(comments_container)

        post = intent.getSerializableExtra(EXTRA_POST) as Post
        MainApplication.instance.plusCommentComponent(post).inject(this)

        initModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainApplication.instance.clearCommentComponent()
    }

    private fun initModel() {
        model = ViewModelProviders.of(this, vmFactory).get(CommentsViewModel::class.java)
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
                    snackbar.show(resources.getString(R.string.comments_download_error))
                }
                RequestState.ERROR_UPLOAD -> {
                    hideLoading()
                    updateHintVisibility(false)
                    snackbar.show(resources.getString(R.string.comment_upload_error))
                }
                RequestState.NO_DATA -> {
                    hideLoading()
                    updateHintVisibility(true)
                }
            }
        })
        model.commentsLiveData.observe(this, Observer<PagedList<Comment>> { comments ->
            val adapter = CommentsAdapter()
            adapter.submitList(comments)
            comments_list.adapter = adapter
            updateHintVisibility(comments.isEmpty())
        })
    }

    private fun updateHintVisibility(visible: Boolean) {
        comments_hint.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun handleSend(text: String) {
        comments_input.setText("")
        comments_input.clearFocus()
        hideSoftKeyboard(this)

        val newComment = Comment().apply {
            this.text = text
            postId = post.id
            author = User("", "").apply {
                login = accProvider.currAccount?.displayName ?: ""
                photo = accProvider.currAccount?.photoUrl ?: ""
            }
        }
        model.addComment(newComment)
    }

    private fun showLoading() {
        comments_progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        comments_progress_bar.visibility = View.GONE
    }

    companion object {
        const val EXTRA_POST = "post"
    }
}
