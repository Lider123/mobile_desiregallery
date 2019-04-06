package com.example.desiregallery.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
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

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var prefs: SharedPreferences
    private var currUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setCurrentUser()
        setContent()
        setToolbar()
        setNavigationMenu()
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_coloronprimary_32dp)
        }
    }

    private fun setNavigationMenu() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when(menuItem.itemId) {
                R.id.nav_profile -> {
                    // TODO: add profile fragment to the main container
                    /* How to add fragment
                    val fragTransaction = supportFragmentManager.beginTransaction()
                    fragTransaction.add(R.id.main_container, Fragment())
                    fragTransaction.commit()*/
                    Toast.makeText(this, "Selected profile", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_settings -> {
                    // TODO
                    Toast.makeText(this, "Selected settings", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_logout -> {
                    // TODO
                    Toast.makeText(this, "Selected logout", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setCurrentUser() {
        Thread(Runnable {
            prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)
            val currLogin = prefs.getString(MainApplication.PREFS_CURR_USER_KEY, null)
            currUser = DGDatabase.getUser(currLogin!!)
            Log.d(TAG, String.format("Current user is %s", currUser?.getLogin()))
        }).start()
    }

    private fun setContent() {
        val model = ViewModelProviders.of(this).get(PostListViewModel::class.java)
        model.posts.observe(this, Observer<List<Post>>{ posts ->
            post_list.layoutManager = LinearLayoutManager(this)
            post_list.adapter = PostAdapter(posts!!, this)
        })
    }
}
