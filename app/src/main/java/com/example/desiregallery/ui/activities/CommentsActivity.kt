package com.example.desiregallery.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.adapters.CommentsAdapter
import com.example.desiregallery.models.Post
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.toolbar_comments.*

/**
 * Activity for watching and sending comments for posts
 *
 * @author babaetskv
 * */
class CommentsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST = "post"
    }

    private lateinit var post: Post
    private lateinit var adapter: CommentsAdapter

    /**
     * Method that creates user interface and list of comments
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        comments_button_back.setOnClickListener { onBackPressed() }
        comments_button_send.setOnClickListener {
            val commentText = comments_input.text.trim()
            if (!commentText.isEmpty())
                handleSend(commentText.toString())
        }

        post = intent.getSerializableExtra(EXTRA_POST) as Post

        comments_list.layoutManager = LinearLayoutManager(this)
        adapter = CommentsAdapter(post.getComments(), this)
        comments_list.adapter = adapter
    }

    /**
     * Method for handling comments send
     * */
    private fun handleSend(text: String) {
        comments_input.setText("")
        comments_input.clearFocus()
        Utils.hideSoftKeyboard(this)

        val comments = post.getComments() as ArrayList
        comments.add(text)
        post.setComments(comments)
        adapter.notifyItemInserted(comments.size-1)
    }
}
