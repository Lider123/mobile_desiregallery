package com.example.desiregallery.ui.screens.profile

import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.auth.EmailAccount
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import com.example.desiregallery.utils.getAgeFromBirthday
import com.example.desiregallery.utils.logError
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.min

/**
 * @author babaetskv on 29.10.19
 */
class ProfilePresenter(
    private val resources: Resources,
    private val accProvider: AccountProvider,
    private val queryService: QueryNetworkService,
    private val auth: FirebaseAuth
) : IProfileContract.Presenter {
    private lateinit var view: IProfileContract.View
    private var editFragment: EditProfileFragment? = null

    override fun attach(view: IProfileContract.View) {
        this.view = view
        accProvider.mObservable.subscribe { updateAll() }
        updateAll()
    }

    override fun onEditClick(fragmentManager: FragmentManager) {
        val account = accProvider.currAccount
        if (account is EmailAccount) {
            editFragment = EditProfileFragment.createInstance(account.user, object: EditProfileFragment.Callback {

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
            val modalFragment = ModalFragmentFactory(fragmentManager)
                .create(editFragment as EditProfileFragment, R.id.profile_container)
            fragmentManager.beginTransaction()
                .add(R.id.profile_container, modalFragment)
                .commit()
        }
    }

    private fun updateStats(login: String) {
        val query = PostsQueryRequest(0, 0, login)
        queryService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                var posts = response.body()
                posts?: return

                if (posts.isEmpty()) {
                    view.updatePostsCount(0)
                    view.updateAverageRating(0f)
                    view.updateNoPostsHintVisibility(true)
                    return
                }

                view.updatePostsCount(posts.size)
                view.updateAverageRating(posts.map { p -> p.rating }.average().toFloat())
                view.updateNoPostsHintVisibility(false)

                posts = posts.sortedByDescending { p -> p.rating }.subList(0, min(3, posts.size))
                view.updatePosts(posts)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                logError(TAG, "Unable to load posts for user $login: ${t.message}")
                view.updatePostsCount(null)
                view.updateAverageRating(null)
            }
        })
    }

    private fun updateAll() {
        val account = accProvider.currAccount
        account?.let {
            view.updateName(it.displayName)

            view.updateAge(getAgeFromBirthday(it.birthday))
            updateStats(it.displayName)
            if (it.photoUrl.isNotEmpty())
                view.updatePhoto(it.photoUrl)

            if (it !is EmailAccount)
                view.updateEditButtonVisibility(false)
        }
    }

    companion object {
        private val TAG = ProfilePresenter::class.java.simpleName
    }
}
