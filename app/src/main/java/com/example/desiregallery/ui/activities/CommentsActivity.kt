package com.example.desiregallery.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.adapters.CommentsAdapter
import com.example.desiregallery.models.Comment
import com.example.desiregallery.models.Post
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.toolbar_comments.*


class CommentsActivity : AppCompatActivity() {

    private lateinit var post: Post
    private lateinit var adapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        comments_button_back.setOnClickListener { onBackPressed() }
        comments_button_send.setOnClickListener {
            val commentText = comments_input.text.trim()
            if (!commentText.isEmpty())
                handleSend(commentText.toString())
        }

        post = intent.getSerializableExtra("post") as Post

        comments_list.layoutManager = LinearLayoutManager(this)
        adapter = CommentsAdapter(post.getComments(), this)
        comments_list.adapter = adapter
    }

    private fun handleSend(text: String) {
        comments_input.setText("")
        comments_input.clearFocus()
        Utils.hideSoftKeyboard(this)

        val comments = post.getComments() as ArrayList
        comments.add(Comment(text))
        post.setComments(comments)
        adapter.notifyItemInserted(comments.size-1)
    }
}
