package com.example.desiregallery.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.adapters.CommentAdapter
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.models.Comment
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import com.example.desiregallery.network.errors.CommentError
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.utils.hideSoftKeyboard
import com.example.desiregallery.viewmodels.CommentListViewModel
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.toolbar_comments.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentsActivity : AppCompatActivity() {
    private val snackbar: SnackbarWrapper by inject { parametersOf(comments_container) }

    private lateinit var post: Post
    private val model: CommentListViewModel by viewModel()
    private val adapter: CommentAdapter by inject { parametersOf(listOf<Comment>()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        comments_button_back.setOnClickListener { onBackPressed() }
        comments_button_send.setOnClickListener {
            val commentText = comments_input.text.trim()
            if (commentText.isNotEmpty())
                handleSend(commentText.toString())
        }
        comments_list.layoutManager = LinearLayoutManager(this)
        comments_list.adapter = adapter

        post = intent.getSerializableExtra(EXTRA_POST) as Post

        setObservers()
        showLoading()
        model.loadComments(post.id)
    }

    private fun setObservers() {
        model.comments.observe(this, Observer<List<Comment>> { comments ->
            adapter.setComments(comments)
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
        hideSoftKeyboard(this)

        val newComment = Comment().apply {
            this.text = text
            postId = post.id
            author = User("", "").apply {
                login = AccountProvider.currAccount?.displayName?: ""
                photo = AccountProvider.currAccount?.photoUrl?: ""
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
