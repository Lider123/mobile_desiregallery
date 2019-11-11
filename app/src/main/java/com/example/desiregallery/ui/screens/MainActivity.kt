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
import com.example.desiregallery.*
import com.example.desiregallery.auth.*
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.ui.screens.auth.AuthActivity
import com.example.desiregallery.ui.screens.profile.ProfileFragment
import com.example.desiregallery.ui.screens.feed.FeedFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val prefs: IDGSharedPreferencesHelper = get()
    private val auth: FirebaseAuth by inject()
    private val accProvider: AccountProvider = get()
    private val baseService: BaseNetworkService by inject()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var headerTextView: TextView
    private lateinit var headerImageView: ImageView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        setNavigationMenu()
        setDefaultFragment()
        setCurrentUser()

        accProvider.mObservable.subscribe { account ->
            headerTextView.text = account.displayName
            if (account.photoUrl.isNotEmpty()) {
                Picasso.with(this)
                    .load(account.photoUrl)
                    .into(headerImageView)
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

        val headerView = navigationView.getHeaderView(0)
        headerTextView = headerView.findViewById(R.id.nav_header_login)
        headerImageView = headerView.findViewById(R.id.nav_header_image)
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
        when (prefs.getAuthMethod()) {
            AuthMethod.EMAIL -> setCurrentEmailUser()
            AuthMethod.VK -> setCurrentVKUser()
            AuthMethod.GOOGLE -> setCurrentGoogleUser()
        }
    }

    private fun setCurrentEmailUser() {
        auth.currentUser?.let {
            baseService.getUser(it.displayName!!).enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    logError(TAG, "Failed to get data for user ${it.displayName}: ${t.message}")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val user = response.body()
                    user?: run {
                        logError(TAG, "Unable to get data for user ${it.displayName}: response received an empty body")
                        return
                    }

                    logInfo(TAG, "Got data for user ${user.login}")
                    accProvider.currAccount = EmailAccount(user, auth)
                }
            })
        }
    }

    private fun setCurrentVKUser() {
        VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_max,sex,bdate"))
            .executeWithListener(object: VKRequest.VKRequestListener() {

                override fun onComplete(response: VKResponse?) {
                    super.onComplete(response)
                    response?: run {
                        logError(
                            TAG,
                            "Failed to get response for user info"
                        )
                        return
                    }

                    val user: VKApiUser = (response.parsedModel as VKList<*>)[0] as VKApiUser
                    accProvider.currAccount = VKAccount(user)
                    accProvider.currAccount?.let { account ->
                        logInfo(TAG, "Got data for user ${account.displayName}")
                        saveUserInfo(User("", "").apply {
                            photo = account.photoUrl
                            login = account.displayName
                        })
                    }
                }

            override fun onError(error: VKError?) {
                super.onError(error)
                logError(TAG, "There was an error with code ${error?.errorCode} while getting user info: ${error?.errorMessage}")
            }
        })
    }

    private fun setCurrentGoogleUser() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?: run {
            logError(TAG, "Failed to get google account")
            return
        }

        accProvider.currAccount = GoogleAccount(account, get())
        accProvider.currAccount?.let { it ->
            logInfo(TAG, "Got data for user ${it.displayName}")
            saveUserInfo(User("", "").apply {
                photo = it.photoUrl
                login = it.displayName
            })
        }
    }

    private fun handleLogout() {
        prefs.clearAuthMethod()
        accProvider.currAccount?.logOut()
        val intent = Intent(this, AuthActivity::class.java).apply {
            putExtra(AuthActivity.EXTRA_IS_LAUNCH, false)
        }
        startActivity(intent)
        finish()
    }

    private fun setDefaultFragment() {
        replaceFragment(FeedFragment(), R.string.app_name)
        navigationView.setCheckedItem(R.id.nav_feed)
    }

    private fun saveUserInfo(user: User) {
        baseService.updateUser(user.login, user).enqueue(object: Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                logInfo(TAG, "Data of user ${user.login} have successfully been saved to firestore")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                logError(TAG, "Unable to save user data to firestore: ${t.message}")
            }
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
