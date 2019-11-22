package com.example.desiregallery.auth

import android.content.Context
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author babaetskv on 20.09.19
 */
class AccountProvider(
    private val context: Context,
    private val prefs: IDGSharedPreferencesHelper,
    private val auth: FirebaseAuth,
    private val networkManager: NetworkManager,
    private val googleClient: GoogleSignInClient
) {
    var currAccount: IAccount? = null
        set(value) {
            mObservable.onNext(Wrapper(value))
            field = value
        }
    val mObservable = PublishSubject.create<Wrapper<IAccount>>()

    fun isAuthorized(): Boolean {
        val authMethod = prefs.getAuthMethod()
        authMethod?: return false
        return true
    }

    fun setCurrentUser() {
        when (prefs.getAuthMethod()) {
            AuthMethod.EMAIL -> setCurrentEmailUser()
            AuthMethod.VK -> setCurrentVKUser()
            AuthMethod.GOOGLE -> setCurrentGoogleUser()
        }
    }

    fun logOut() {
        prefs.clearAuthMethod()
        currAccount?.logOut()
    }

    private fun setCurrentEmailUser() {
        auth.currentUser?.let {
            GlobalScope.launch(Dispatchers.Main) {
                val login = it.displayName!!
                when (val result = networkManager.getUser(login)) {
                    is Result.Success -> {
                        Timber.i("Successfully got user $login")
                        currAccount = EmailAccount(result.data, auth)
                    }
                    is Result.Error -> Timber.e(result.exception, "Failed to get user $login")
                }
            }
        }
    }

    private fun setCurrentVKUser() {
        VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_max,sex,bdate"))
            .executeWithListener(object: VKRequest.VKRequestListener() {

                override fun onComplete(response: VKResponse?) {
                    super.onComplete(response)
                    response ?: run {
                        Timber.e("Failed to get response for user info")
                        return
                    }

                    val user: VKApiUser = (response.parsedModel as VKList<*>)[0] as VKApiUser
                    currAccount = VKAccount(user)
                    currAccount?.let { account ->
                        Timber.i("Got data for user ${account.displayName}")
                        GlobalScope.launch(Dispatchers.Main) {
                            val vkUser = User("", "").apply {
                                photo = account.photoUrl
                                login = account.displayName
                            }
                            when (val result = networkManager.updateUser(vkUser)) {
                                is Result.Success -> Timber.i("User ${vkUser.login} has been successfully updated")
                                is Result.Error -> Timber.e(result.exception, "Failed to update user ${vkUser.login}")
                            }
                        }
                    }
                }

                override fun onError(error: VKError?) {
                    super.onError(error)
                    Timber.e("There was an error with code ${error?.errorCode} while getting user info: ${error?.errorMessage}")
                }
            })
    }

    private fun setCurrentGoogleUser() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?: run {
            Timber.e("Failed to get google account")
            return
        }

        currAccount = GoogleAccount(account, googleClient)
        currAccount?.let { it ->
            Timber.i("Got data for user ${it.displayName}")
            GlobalScope.launch(Dispatchers.Main) {
                val googleUser = User("", "").apply {
                    photo = it.photoUrl
                    login = it.displayName
                }
                when (val result = networkManager.updateUser(googleUser)) {
                    is Result.Success -> Timber.i("User ${googleUser.login} has been successfully updated")
                    is Result.Error -> Timber.e(result.exception, "Failed to update user ${googleUser.login}")
                }
            }
        }
    }

    data class Wrapper<T>(val value: T?)
}
