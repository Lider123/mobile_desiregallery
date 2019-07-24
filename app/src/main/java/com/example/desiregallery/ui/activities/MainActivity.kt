package com.example.desiregallery.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.desiregallery.*
import com.example.desiregallery.database.DGDatabase
import com.example.desiregallery.models.User
import com.example.desiregallery.ui.fragments.FeedFragment
import com.example.desiregallery.ui.fragments.ProfileFragment
import com.example.desiregallery.ui.fragments.SettingsFragment


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
        currUser?.let {
            val headerView = navigationView.getHeaderView(0)
            val headerTextView = headerView.findViewById<TextView>(R.id.nav_header_login)
            headerImageView = headerView.findViewById(R.id.nav_header_image)
            headerTextView.text = it.getLogin()
            if (it.getPhoto().isNotEmpty())
                updateNavHeaderPhoto()
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

    private fun setCurrentUser() {
        val currLogin = prefs.getString(MainApplication.PREFS_CURR_USER_KEY, null)
        currLogin?.let {
            currUser = DGDatabase.getUser(it)
            Log.d(TAG, String.format("Current user is %s", currUser?.getLogin()))
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

    fun getCurrUser() = currUser

    fun updateNavHeaderPhoto() {
        currUser?.let { headerImageView.setImageBitmap(Utils.base64ToBitmap(it.getPhoto())) }
    }
}
