package com.example.desiregallery.presenters

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.auth.EmailAccount
import com.example.desiregallery.logging.logError
import com.example.desiregallery.logging.logInfo
import com.example.desiregallery.models.User
import com.example.desiregallery.network.baseService
import com.example.desiregallery.storage.IStorageHelper
import com.example.desiregallery.ui.contracts.IProfileContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author babaetskv on 29.10.19
 */
class ProfilePresenter(
    private val view: IProfileContract.View,
    private val accProvider: AccountProvider,
    private val auth: FirebaseAuth,
    private val storageHelper: IStorageHelper
) : IProfileContract.Presenter {

    override var infoChanged = false
        private set

    override fun attach(resources: Resources) {
        val account = accProvider.currAccount
        account?.let {
            view.updateTitle(it.displayName)

            val notSpecified = resources.getString(R.string.not_specified)
            view.updateGender(if (it.gender.isNotEmpty()) it.gender else notSpecified)
            view.updateBirthday(if (it.birthday.isNotEmpty()) it.birthday else notSpecified)
            if (it.photoUrl.isNotEmpty())
                view.updatePhoto(it.photoUrl)

            if (it !is EmailAccount)
                view.updatePhotoFabVisibility(false)
        }
    }

    override fun onGenderClick(context: Context) {
        if (accProvider.currAccount is EmailAccount)
            editGender(context)
    }

    override fun onBirthdayClick(context: Context) {
        if (accProvider.currAccount is EmailAccount)
            editBirthday(context)
    }

    override fun updateProfile(activity: Activity) {
        val account = accProvider.currAccount
        if (account is EmailAccount) {
            val emailUser = account.user
            baseService.updateUser(emailUser.login, emailUser).enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    logError(TAG, "Unable to update user ${emailUser.login}: ${t.message}")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful)
                        logInfo(TAG, "VKUser ${emailUser.login} has been successfully updated")
                }
            })

            val firebaseUser = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(emailUser.login)
                .setPhotoUri(Uri.parse(emailUser.photo))
                .build()
            firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful)
                    logInfo(TAG, String.format("Data of user %s have successfully been saved to firebase auth", emailUser.login))
                else
                    logError(TAG, "Unable to save user data to firebase auth: ${task.exception?.message}")
            }
        }

        infoChanged = false
    }

    override fun uploadPhoto(imageUri: Uri, resolver: ContentResolver, onFailure: () -> Unit, onComplete: () -> Unit) {
        val account = accProvider.currAccount as? EmailAccount ?: return
        val emailUser = account.user

        val istream = resolver.openInputStream(imageUri)
        val selectedImage = BitmapFactory.decodeStream(istream)
        storageHelper.uploadProfileImage(selectedImage, emailUser.login, object: IStorageHelper.Callback {

            override fun onComplete(resultUrl: String) {
                logInfo(TAG, "Image for user ${emailUser.login} successfully uploaded")
                emailUser.photo = resultUrl
                accProvider.currAccount = EmailAccount(emailUser, auth)
                view.updatePhoto(selectedImage)
                onComplete()
                infoChanged = true
            }

            override fun onFailure(error: Exception) {
                logError(TAG, "Failed to upload image for user ${emailUser.login}: ${error.message}")
                onFailure()
            }
        })
    }

    private fun editBirthday(context: Context) {
        val account = accProvider.currAccount as? EmailAccount ?: return
        val emailUser = account.user

        val currBirthday = emailUser.birthday
        val cal = Calendar.getInstance()
        if (currBirthday.isNotEmpty()) {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            cal.time = sdf.parse(currBirthday)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val birthday = context.getString(R.string.date_format, dayOfMonth, monthOfYear+1, year)
            if (birthday != emailUser.birthday) {
                emailUser.birthday = birthday
                accProvider.currAccount = EmailAccount(emailUser, auth)
                view.updateBirthday(birthday)
                infoChanged = true
            }
        }
        DatePickerDialog(context,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun editGender(context: Context) {
        val account = accProvider.currAccount as? EmailAccount ?: return
        val emailUser = account.user
        val gender = emailUser.gender
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.gender_dialog_title)
        val values = context.resources.getStringArray(R.array.gender)
        val checkedItem = if (gender.isNotEmpty()) values.indexOf(gender) else -1

        builder.setSingleChoiceItems(values, checkedItem) { dialog, item ->
            emailUser.gender = values[item]
            accProvider.currAccount = EmailAccount(emailUser, auth)
            view.updateGender(values[item])
            infoChanged = true
            dialog.dismiss()
        }
        val genderDialog = builder.create()
        genderDialog.show()
    }

    companion object {
        private val TAG = ProfilePresenter::class.java.simpleName
    }
}