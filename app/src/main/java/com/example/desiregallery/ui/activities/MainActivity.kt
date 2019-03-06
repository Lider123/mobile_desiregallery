package com.example.desiregallery.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.desiregallery.R
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.helpers.ModelGenerator
import com.example.desiregallery.models.Post
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var posts : List<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        posts = ModelGenerator.getPosts()

        post_list.layoutManager = LinearLayoutManager(this)
        post_list.adapter = PostAdapter(posts, this)
    }
}
