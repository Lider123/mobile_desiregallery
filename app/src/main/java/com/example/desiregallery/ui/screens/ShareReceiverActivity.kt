package com.example.desiregallery.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.postcreation.IPostCreationListener
import com.example.desiregallery.ui.screens.postcreation.PostCreationFragment
import com.example.desiregallery.ui.screens.auth.ILoginListener
import com.example.desiregallery.ui.screens.auth.LoginFragment
import com.example.desiregallery.utils.getBitmapFromUri
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * @author babaetskv on 15.11.19
 */
class ShareReceiverActivity : AppCompatActivity(), ILoginListener, IPostCreationListener {
    @Inject
    lateinit var networkManager: NetworkManager
    @Inject
    lateinit var accProvider: AccountProvider

    private val mDisposable = CompositeDisposable()
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    private lateinit var loginFragment: LoginFragment
    private lateinit var postCreationFragment: PostCreationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_receiver)
        MainApplication.appComponent.inject(this)

        intent ?: return

        if (Intent.ACTION_SEND == intent.action && true == intent.type?.startsWith("image/")) {
            mDisposable.add(
                accProvider.mObservable.subscribe {
                    handlePublishImage(intent)
                    mDisposable.dispose()
                }
            )

            if (!accProvider.isAuthorized()) {
                loginFragment = LoginFragment()
                supportFragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment).commit()
            }
            else
                accProvider.setCurrentUser()

        }
    }

    override fun onStop() {
        super.onStop()
        mDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }

    override fun onSuccessfulLogin() {
        supportFragmentManager.beginTransaction().remove(loginFragment).commit()
        accProvider.setCurrentUser()
    }

    override fun onPostCreationSubmit(post: Post) {
        coroutineScope.launch(Dispatchers.Main) {
            when (val result = networkManager.createPost(post)) {
                is Result.Success -> Timber.i("Post ${post.id} has been successfully created")
                is Result.Error -> Timber.e(result.exception, "Failed to create post")
            }
            goToMainActivity()
            finish()
        }
    }

    override fun onPostCreationCancel() = finish()

    private fun handlePublishImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { uri ->
            val bitmap = getBitmapFromUri(uri, contentResolver)
            postCreationFragment = PostCreationFragment.createInstance(bitmap)
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, postCreationFragment).commit()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
