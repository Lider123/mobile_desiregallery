package com.example.desiregallery.ui.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.ui.screens.auth.LoginActivity
import com.example.desiregallery.ui.screens.profile.ProfileFragment
import com.example.desiregallery.ui.screens.feed.FeedFragment
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var accProvider: AccountProvider

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var headerTextView: TextView
    private lateinit var headerImageView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var mDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainApplication.appComponent.inject(this)

        setToolbar()
        setNavigationMenu()
        setDefaultFragment()
    }

    override fun onStart() {
        super.onStart()
        mDisposable = accProvider.mObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.value }
            .subscribe { account ->
                headerTextView.text = account?.displayName ?: getString(R.string.login)
                if (account == null || account.photoUrl.isEmpty()) {
                    Picasso.with(this)
                        .load(R.drawable.material)
                        .resize(200, 200)
                        .into(headerImageView)
                } else {
                    Picasso.with(this)
                        .load(account.photoUrl)
                        .resize(200, 200)
                        .placeholder(R.drawable.material)
                        .error(R.drawable.image_error)
                        .into(headerImageView)
                }
            }

        accProvider.setCurrentUser()
    }

    override fun onStop() {
        super.onStop()
        if (!mDisposable.isDisposed) mDisposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            drawerLayout.openDrawer(GravityCompat.START)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun setToolbar() {
        toolbar = findViewById<Toolbar>(R.id.main_toolbar).apply {
            title = resources.getString(R.string.app_name)
        }
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_coloronprimary_32dp)
        }
    }

    private fun setNavigationMenu() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById<NavigationView>(R.id.nav_view).apply {
            itemIconTintList = null
            setNavigationItemSelectedListener { menuItem ->
                selectDrawerItem(menuItem)
                menuItem.isChecked = true
                drawerLayout.closeDrawers()
                return@setNavigationItemSelectedListener true
            }
        }

        navigationView.getHeaderView(0).also {
            headerTextView = it.findViewById(R.id.nav_header_login)
            headerImageView = it.findViewById(R.id.nav_header_image)
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        when (menuItem.itemId) {
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

    private fun handleLogout() {
        accProvider.logOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setDefaultFragment() {
        replaceFragment(FeedFragment(), R.string.app_name)
        navigationView.setCheckedItem(R.id.nav_feed)
    }
}
