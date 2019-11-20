package com.example.desiregallery.auth

import android.content.Context
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
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
                        logInfo(TAG, "Successfully got user $login")
                        currAccount = EmailAccount(result.data, auth)
                    }
                    is Result.Error -> logError(TAG, result.exception.message ?: "Failed to get user $login")
                }
            }
        }
    }

    private fun setCurrentVKUser() {
        VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_max,sex,bdate"))
            .executeWithListener(object: VKRequest.VKRequestListener() {

                override fun onComplete(response: VKResponse?) {
                    super.onComplete(response)
                    response?: run {
                        logError(TAG, "Failed to get response for user info")
                        return
                    }

                    val user: VKApiUser = (response.parsedModel as VKList<*>)[0] as VKApiUser
                    currAccount = VKAccount(user)
                    currAccount?.let { account ->
                        logInfo(TAG, "Got data for user ${account.displayName}")
                        GlobalScope.launch(Dispatchers.Main) {
                            val vkUser = User("", "").apply {
                                photo = account.photoUrl
                                login = account.displayName
                            }
                            when (val result = networkManager.updateUser(vkUser)) {
                                is Result.Success -> logInfo(TAG, "User ${vkUser.login} has been successfully updated")
                                is Result.Error -> logError(TAG, result.exception.message ?: "Failed to update user ${vkUser.login}")
                            }
                        }
                    }
                }

                override fun onError(error: VKError?) {
                    super.onError(error)
                    logError(TAG, "There was an error with code ${error?.errorCode} while getting user info: ${error?.errorMessage}")
                }
            })
    }

    private fun setCurrentGoogleUser() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?: run {
            logError(TAG, "Failed to get google account")
            return
        }

        currAccount = GoogleAccount(account, googleClient)
        currAccount?.let { it ->
            logInfo(TAG, "Got data for user ${it.displayName}")
            GlobalScope.launch(Dispatchers.Main) {
                val googleUser = User("", "").apply {
                    photo = it.photoUrl
                    login = it.displayName
                }
                when (val result = networkManager.updateUser(googleUser)) {
                    is Result.Success -> logInfo(TAG, "User ${googleUser.login} has been successfully updated")
                    is Result.Error -> logError(TAG, result.exception.message ?: "Failed to update user ${googleUser.login}")
                }
            }
        }
    }

    companion object {
        private val TAG = AccountProvider::class.java.simpleName
    }

    data class Wrapper<T>(val value: T?)
}
