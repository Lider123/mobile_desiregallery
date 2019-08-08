package com.example.desiregallery.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.desiregallery.*
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import com.example.desiregallery.ui.fragments.FeedFragment
import com.example.desiregallery.ui.fragments.ProfileFragment
import com.example.desiregallery.ui.fragments.SettingsFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var headerImageView: ImageView
    private lateinit var toolbar: Toolbar

    private lateinit var prefs: SharedPreferences
    private var currUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)

        setCurrentUser()
        setToolbar()
        setNavigationMenu()
        setDefaultFragment()
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

    private fun setToolbar() {
        toolbar = findViewById(R.id.main_toolbar)
        toolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_coloronprimary_32dp)
        }
    }

    private fun setNavigationMenu() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        when(menuItem.itemId) {
            R.id.nav_profile -> replaceFragment(ProfileFragment(), menuItem.title)
            R.id.nav_feed -> replaceFragment(FeedFragment(), R.string.app_name)
            R.id.nav_settings -> replaceFragment(SettingsFragment(), menuItem.title)
            R.id.nav_logout -> handleLogout()
        }
    }

    private fun replaceFragment(fragment: Fragment, title: CharSequence) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
        toolbar.title = title
    }

    private fun replaceFragment(fragment: Fragment, id: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
        toolbar.title = resources.getString(id)
    }

    fun getCurrUser() = currUser

    private fun setCurrentUser() {
        MainApplication.getAuth().currentUser?.let {
            DGNetwork.getBaseService().getUser(it.displayName!!).enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "Failed to get data for user ${it.displayName}: ${t.message}")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.body()?: run {
                        Log.e(TAG, "Unable to get data for user ${it.displayName}: response received an empty body")
                        return
                    }

                    currUser = response.body()
                    Log.d(TAG, String.format("Got data for user ${it.displayName}"))

                    val headerView = navigationView.getHeaderView(0)
                    val headerTextView = headerView.findViewById<TextView>(R.id.nav_header_login)
                    headerImageView = headerView.findViewById(R.id.nav_header_image)
                    headerTextView.text = currUser?.login
                    if (currUser?.photo!!.isNotEmpty())
                        updateNavHeaderPhoto()
                }
            })
        }
    }

    private fun handleLogout() {
        prefs.edit().remove(MainApplication.PREFS_CURR_USER_KEY).apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setDefaultFragment() {
        replaceFragment(FeedFragment(), R.string.app_name)
        navigationView.setCheckedItem(R.id.nav_feed)
    }

    fun updateNavHeaderPhoto() {
        currUser?.let { Picasso.with(this).load(it.photo).into(headerImageView) }
    }
}
