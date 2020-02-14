package com.example.desiregallery.ui.screens.profile

import android.os.Bundle
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.auth.EmailAccount
import com.example.desiregallery.data.models.User
import com.example.desiregallery.ui.screens.base.StyledActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.toolbar_profile.*
import javax.inject.Inject

class ProfileActivity : StyledActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        MainApplication.appComponent.inject(this)

        profile_button_back.setOnClickListener { onBackPressed() }

        val user = intent.extras?.getSerializable(EXTRA_USER) as User
        val account = EmailAccount(user, auth)
        val fragment = ProfileFragment.createInstance(account)
        supportFragmentManager.beginTransaction()
            .add(R.id.profile_fragment_container, fragment)
            .commit()
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}
