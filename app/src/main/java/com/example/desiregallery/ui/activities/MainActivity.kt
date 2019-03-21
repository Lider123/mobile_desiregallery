package com.example.desiregallery.ui.activities

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

        lateinit var layoutManager: RecyclerView.LayoutManager
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = GridLayoutManager(this, 2)
        }
        else {
            layoutManager = LinearLayoutManager(this)
        }

        post_list.layoutManager = layoutManager
        post_list.adapter = PostAdapter(posts, this)
    }
}
