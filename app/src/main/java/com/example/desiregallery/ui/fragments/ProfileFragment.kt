package com.example.desiregallery.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.ui.activities.MainActivity
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.Utils
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.auth.EmailAccount
import com.example.desiregallery.logging.DGLogger
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    companion object {
        private val TAG = ProfileFragment::class.java.simpleName
    }

    private var infoChanged = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val toolbarProfile = root.findViewById<Toolbar>(R.id.profile_toolbar)
        val imageView = root.findViewById<ImageView>(R.id.profile_image_backdrop)
        val fab = root.findViewById<FloatingActionButton>(R.id.profile_fab)

        val account = AccountProvider.currAccount
        account?.let {
            toolbarProfile.title = it.displayName

            val notSpecified = getString(R.string.not_specified)
            root.profile_gender.text = if (it.gender.isNotEmpty()) it.gender else notSpecified
            root.profile_birthday.text = if (it.birthday.isNotEmpty()) it.birthday else notSpecified
            if (it.photoUrl.isNotEmpty())
                Picasso.with(activity).load(it.photoUrl).into(imageView)

            if (it is EmailAccount) {
                fab.setOnClickListener { CropImage.activity().start(requireContext(), this) }
                root.profile_gender_view.setOnClickListener { editGender() }
                root.profile_birthday_view.setOnClickListener { editBirthday() }
            }
            else
                fab.visibility = View.GONE
        }

        return root
    }

    override fun onPause() {
        super.onPause()
        if (!infoChanged)
            return

        val account = AccountProvider.currAccount
        if (account is EmailAccount) {
            val emailUser = account.user
            DGNetwork.getBaseService().updateUser(emailUser.login, emailUser).enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    DGLogger.logError(TAG, "Unable to update user ${emailUser.login}: ${t.message}")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful)
                        DGLogger.logInfo(TAG, "VKUser ${emailUser.login} has been successfully updated")
                }
            })

            val firebaseUser = MainApplication.auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(emailUser.login)
                .setPhotoUri(Uri.parse(emailUser.photo))
                .build()
            firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful)
                    DGLogger.logInfo(TAG, String.format("Data of user %s have successfully been saved to firebase auth", emailUser.login))
                else
                    DGLogger.logError(TAG, "Unable to save user data to firebase auth: ${task.exception?.message}")
            }
        }

        infoChanged = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val account = AccountProvider.currAccount as? EmailAccount ?: return

            val emailUser = account.user

            val imageUri = CropImage.getActivityResult(data).uri
            val istream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(istream)

            val imageRef = MainApplication.storage.getReferenceFromUrl(MainApplication.STORAGE_URL).child("${MainApplication.STORAGE_PROFILE_IMAGES_DIR}/${emailUser.login}.jpg")
            val uploadTask = imageRef.putBytes(Utils.bitmapToBytes(selectedImage))
            uploadTask.addOnFailureListener { error ->
                DGLogger.logError(TAG, "Failed to upload image for user ${emailUser.login}: ${error.message}")
                Toast.makeText(activity, R.string.profile_image_upload_failure, Toast.LENGTH_LONG).show()
            }.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    DGLogger.logError(TAG, "Image for user ${emailUser.login} has not been uploaded")
                    Toast.makeText(activity, R.string.profile_image_upload_failure, Toast.LENGTH_LONG).show()
                    return@addOnCompleteListener
                }

                DGLogger.logInfo(TAG, "Image for user ${emailUser.login} successfully uploaded")
                imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                    emailUser.photo = uriTask.result.toString()
                    AccountProvider.currAccount = EmailAccount(emailUser)
                    profile_image_backdrop.setImageBitmap(selectedImage)
                    (activity as MainActivity).updateNavHeaderPhoto()
                    infoChanged = true
                }
            }
        }
    }

    private fun editBirthday() {
        val account = AccountProvider.currAccount as? EmailAccount ?: return

        val emailUser = account.user

        val currBirthday = emailUser.birthday
        val cal = Calendar.getInstance()
        if (currBirthday.isNotEmpty()) {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            cal.time = sdf.parse(currBirthday)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val birthday = getString(R.string.date_format, dayOfMonth, monthOfYear+1, year)
            if (birthday != emailUser.birthday) {
                emailUser.birthday = birthday
                AccountProvider.currAccount = EmailAccount(emailUser)
                profile_birthday.text = birthday
                infoChanged = true
            }
        }
        DatePickerDialog(context!!,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun editGender() {
        val account = AccountProvider.currAccount as? EmailAccount ?: return

        val emailUser = account.user
        val gender = emailUser.gender
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.gender_dialog_title)
        val values = resources.getStringArray(R.array.gender)
        val checkedItem = if (gender.isNotEmpty()) values.indexOf(gender) else -1

        builder.setSingleChoiceItems(values, checkedItem) { dialog, item ->
            emailUser.gender = values[item]
            AccountProvider.currAccount = EmailAccount(emailUser)
            profile_gender.text = values[item]
            infoChanged = true
            dialog.dismiss()
        }
        val genderDialog = builder.create()
        genderDialog.show()
    }
}
