package com.example.desiregallery.ui.screens.profile

import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.auth.EmailAccount
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import com.example.desiregallery.utils.getAgeFromBirthday
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.min

/**
 * @author babaetskv on 29.10.19
 */
class ProfilePresenter(
    private val resources: Resources,
    private val accProvider: AccountProvider,
    private val networkManager: NetworkManager,
    private val auth: FirebaseAuth
) : IProfileContract.Presenter {
    private lateinit var view: IProfileContract.View
    private var editFragment: EditProfileFragment? = null

    private val mDisposable = CompositeDisposable()

    override fun attach(view: IProfileContract.View) {
        this.view = view
        mDisposable.add(
            accProvider.mObservable.subscribe { updateAll() }
        )
        updateAll()
    }

    override fun detach() = mDisposable.dispose()

    override fun onEditClick(fragmentManager: FragmentManager) {
        val account = accProvider.currAccount
        if (account is EmailAccount) {
            editFragment = EditProfileFragment.createInstance(
                account.user,
                object : EditProfileFragment.Callback {

                    override fun onDoneClick(user: User) {
                        fragmentManager.beginTransaction()
                            .remove(editFragment!!)
                            .commit()
                        accProvider.currAccount = EmailAccount(user, auth)
                        updateAll()
                        view.showMessage(resources.getString(R.string.changes_saved))
                    }

                    override fun onCancelClick() {
                        fragmentManager.beginTransaction()
                            .remove(editFragment!!)
                            .commit()
                    }
                })
            val modalFragment = ModalFragmentFactory(fragmentManager).create(
                editFragment as EditProfileFragment,
                R.id.profile_container
            )
            fragmentManager.beginTransaction()
                .add(R.id.profile_container, modalFragment)
                .commit()
        }
    }

    private fun updateStats(login: String) {
        val query = PostsQueryRequest(0, 0, login)
        GlobalScope.launch(Dispatchers.Main) {
            when (val result = networkManager.getPosts(query)) {
                is Result.Success -> {
                    Timber.i("Successfully got posts")
                    var posts = result.data

                    if (posts.isEmpty()) {
                        view.updatePostsCount(0)
                        view.updateAverageRating(0f)
                        view.updateNoPostsHintVisibility(true)
                        return@launch
                    }

                    view.updatePostsCount(posts.size)
                    view.updateAverageRating(posts.map { p -> p.rating }.average().toFloat())
                    view.updateNoPostsHintVisibility(false)

                    posts =
                        posts.sortedByDescending { p -> p.rating }.subList(0, min(3, posts.size))
                    view.updatePosts(posts)
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Failed to get posts")
                    view.updatePostsCount(null)
                    view.updateAverageRating(null)
                }
            }
        }
    }

    private fun updateAll() {
        val account = accProvider.currAccount
        account?.let {
            view.updateName(it.displayName)

            view.updateAge(getAgeFromBirthday(it.birthday))
            updateStats(it.displayName)
            if (it.photoUrl.isNotEmpty()) view.updatePhoto(it.photoUrl)

            if (it !is EmailAccount) view.updateEditButtonVisibility(false)
        }
    }
}
