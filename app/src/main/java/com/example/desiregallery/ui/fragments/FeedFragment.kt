package com.example.desiregallery.ui.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.models.Post
import com.example.desiregallery.viewmodels.PostListViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setContent()
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    private fun setContent() {
        val model = ViewModelProviders.of(this).get(PostListViewModel::class.java)
        model.getPosts().observe(this, Observer<List<Post>>{ posts ->
            post_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            post_list.adapter = PostAdapter(posts!!, activity!!)
        })
    }
}
