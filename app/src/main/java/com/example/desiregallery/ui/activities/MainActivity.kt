package com.example.desiregallery.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.database.DGDatabase
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import com.example.desiregallery.viewmodels.PostListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var prefs: SharedPreferences
    private var currUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setCurrentUser()

        val model = ViewModelProviders.of(this).get(PostListViewModel::class.java)
        model.posts.observe(this, Observer<List<Post>>{ posts ->
            post_list.layoutManager = LinearLayoutManager(this)
            post_list.adapter = PostAdapter(posts!!, this)
        })
    }

    private fun setCurrentUser() {
        Thread(Runnable {
            prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)
            val currLogin = prefs.getString(MainApplication.PREFS_CURR_USER_KEY, null)
            currUser = DGDatabase.getUser(currLogin!!)
            Log.d(TAG, String.format("Current user is %s", currUser?.getLogin()))
        }).start()
    }
}
