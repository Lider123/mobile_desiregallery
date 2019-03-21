package com.example.desiregallery.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.models.Post
import com.example.desiregallery.viewmodels.PostListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var posts : List<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model = ViewModelProviders.of(this).get(PostListViewModel::class.java)
        model.posts.observe(this, Observer<List<Post>>{ posts ->
            post_list.layoutManager = LinearLayoutManager(this)
            post_list.adapter = PostAdapter(posts!!, this)
        })
    }
}
