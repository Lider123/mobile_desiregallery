package com.example.desiregallery.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.adapters.CommentsAdapter
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.models.Comment
import com.example.desiregallery.models.Post
import com.example.desiregallery.network.errors.CommentError
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.viewmodels.CommentListViewModel
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.toolbar_comments.*

class CommentsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST = "post"
    }

    private lateinit var snackbar: SnackbarWrapper

    private lateinit var post: Post
    private lateinit var model: CommentListViewModel
    private lateinit var adapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        snackbar = SnackbarWrapper(comments_container)
        comments_button_back.setOnClickListener { onBackPressed() }
        comments_button_send.setOnClickListener {
            val commentText = comments_input.text.trim()
            if (commentText.isNotEmpty())
                handleSend(commentText.toString())
        }

        post = intent.getSerializableExtra(EXTRA_POST) as Post

        initModel()
        showLoading()
        model.loadComments(post.id)
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(CommentListViewModel::class.java)
        model.comments.observe(this, Observer<List<Comment>> { comments ->
            comments_list.layoutManager = LinearLayoutManager(this)
            adapter = CommentsAdapter(comments, this)
            comments_list.adapter = adapter
            hideLoading()
            updateHintVisibility(comments.isNotEmpty())
        })
        model.error.observe(this, Observer<CommentError?> { error ->
            error?.let {
                hideLoading()
                updateHintVisibility(false)
                snackbar.show(error.message)
            }
        })
    }

    private fun updateHintVisibility(gotComments: Boolean) {
        comments_hint.visibility = if (gotComments) View.GONE else View.VISIBLE
    }

    private fun handleSend(text: String) {
        comments_input.setText("")
        comments_input.clearFocus()
        Utils.hideSoftKeyboard(this)

        val newComment = Comment().apply {
            this.text = text
            postId = post.id
            author = AccountProvider.currAccount?.displayName?: ""
        }
        model.addComment(newComment)
    }

    private fun showLoading() {
        comments_progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        comments_progress_bar.visibility = View.GONE
    }
}
