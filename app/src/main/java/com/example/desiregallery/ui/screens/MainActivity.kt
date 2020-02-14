package com.example.desiregallery.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.ui.screens.auth.LoginActivity
import com.example.desiregallery.ui.screens.base.BaseActivity
import com.example.desiregallery.ui.IOnBackPressed
import com.example.desiregallery.ui.screens.base.StyledActivity
import com.example.desiregallery.ui.screens.feed.FeedFragment
import com.example.desiregallery.ui.screens.profile.ProfileFragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MainActivity : StyledActivity() {
    @Inject
    lateinit var accProvider: AccountProvider

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var headerTextView: TextView
    private lateinit var headerImageView: ImageView
    private lateinit var mDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainApplication.appComponent.inject(this)

        setToolbar(R.id.main_toolbar, getString(R.string.app_name), true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_coloronprimary_32dp)
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

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.main_container)?.let {
            if (it !is IOnBackPressed || it.onBackPressed().not()) super.onBackPressed()
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
            R.id.nav_profile -> replaceFragment(
                ProfileFragment.createInstance(accProvider.currAccount!!),
                menuItem.title
            )
            R.id.nav_feed -> replaceFragment(FeedFragment(), R.string.app_name)
            R.id.nav_settings -> replaceFragment(SettingsFragment(), menuItem.title)
            R.id.nav_logout -> handleLogout()
        }
    }

    private fun replaceFragment(fragment: Fragment, title: CharSequence) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
        supportActionBar?.title = title
    }

    private fun replaceFragment(fragment: Fragment, id: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
        supportActionBar?.title = resources.getString(id)
    }

    private fun handleLogout() {
        accProvider.logOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setDefaultFragment() {
        if (prefsHelper.startWithSettings) {
            replaceFragment(SettingsFragment(), R.string.Settings)
            navigationView.setCheckedItem(R.id.nav_settings)
            prefsHelper.startWithSettings = false
        } else {
            replaceFragment(FeedFragment(), R.string.app_name)
            navigationView.setCheckedItem(R.id.nav_feed)
        }
    }
}
