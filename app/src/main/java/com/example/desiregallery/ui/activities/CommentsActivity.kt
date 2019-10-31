package com.example.desiregallery.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.adapters.CommentAdapter
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.models.Comment
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import com.example.desiregallery.network.RequestStatus
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.utils.hideSoftKeyboard
import com.example.desiregallery.viewmodels.CommentListViewModel
import com.example.desiregallery.viewmodels.CommentListViewModelFactory
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.toolbar_comments.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CommentsActivity : AppCompatActivity() {
    private val snackbar: SnackbarWrapper by inject { parametersOf(comments_container) }

    private lateinit var post: Post
    private lateinit var model: CommentListViewModel

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

        post = intent.getSerializableExtra(EXTRA_POST) as Post

        initModel()
    }

    private fun initModel() {
        model = ViewModelProviders.of(this, CommentListViewModelFactory(this.application, post.id)).get(CommentListViewModel::class.java)
        model.requestStatus.observe(this, Observer { status ->
            status?: return@Observer

            when(status) {
                RequestStatus.DOWNLOADING -> {
                    showLoading()
                    updateHintVisibility(false)
                }
                RequestStatus.SUCCESS -> {
                    hideLoading()
                    updateHintVisibility(false)
                }
                RequestStatus.ERROR_DOWNLOAD -> {
                    hideLoading()
                    updateHintVisibility(true)
                    snackbar.show(status.message)
                }
                RequestStatus.ERROR_UPLOAD -> {
                    hideLoading()
                    updateHintVisibility(true)
                    snackbar.show(status.message)
                }
            }
        })
        model.pagedListLiveData.observe(this, Observer<PagedList<Comment>> { comments ->
            val adapter = CommentAdapter()
            adapter.submitList(comments)
            comments_list.adapter = adapter
            hideLoading()
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
