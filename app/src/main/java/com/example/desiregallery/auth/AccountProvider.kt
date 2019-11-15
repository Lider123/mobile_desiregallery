package com.example.desiregallery.auth

import android.content.Context
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkUtils
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 20.09.19
 */
class AccountProvider(
    private val context: Context,
    private val prefs: IDGSharedPreferencesHelper,
    private val auth: FirebaseAuth,
    private val baseService: BaseNetworkService,
    private val networkUtils: NetworkUtils,
    private val googleClient: GoogleSignInClient
) {
    var currAccount: IAccount? = null
        set(value) {
            mObservable.onNext(Wrapper(value))
            field = value
        }
    val mObservable: PublishSubject<Wrapper<IAccount>> = PublishSubject.create<Wrapper<IAccount>>()

    data class Wrapper<T>(val value: T?)

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
                    currAccount = EmailAccount(user, auth)
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
                    currAccount = VKAccount(user)
                    currAccount?.let { account ->
                        logInfo(TAG, "Got data for user ${account.displayName}")
                        networkUtils.saveUserInfo(User("", "").apply {
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
        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?: run {
            logError(TAG, "Failed to get google account")
            return
        }

        currAccount = GoogleAccount(account, googleClient)
        currAccount?.let { it ->
            logInfo(TAG, "Got data for user ${it.displayName}")
            networkUtils.saveUserInfo(User("", "").apply {
                photo = it.photoUrl
                login = it.displayName
            })
        }
    }

    companion object {
        private val TAG = AccountProvider::class.java.simpleName
    }
}
