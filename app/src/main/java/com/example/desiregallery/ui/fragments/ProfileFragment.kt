package com.example.desiregallery.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.desiregallery.R
import com.example.desiregallery.ui.activities.MainActivity
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.Utils
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
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val toolbarProfile = root.findViewById<Toolbar>(R.id.profile_toolbar)
        val imageView = root.findViewById<ImageView>(R.id.profile_image_backdrop)
        val fab = root.findViewById<FloatingActionButton>(R.id.profile_fab)

        val user = (activity as MainActivity).getCurrUser()
        user?.let {
            toolbarProfile.title = it.login
            fab.setOnClickListener { CropImage.activity().start(requireContext(), this) }

            val notSpecified = getString(R.string.not_specified)
            root.profile_gender.text = if (it.gender.isNotEmpty()) it.gender else notSpecified
            root.profile_birthday.text = if (it.birthday.isNotEmpty()) it.birthday else notSpecified
            if (it.photo.isNotEmpty())
                Picasso.with(activity).load(it.photo).into(imageView)

            root.profile_gender_view.setOnClickListener { editGender() }
            root.profile_birthday_view.setOnClickListener { editBirthday() }
        }
        this.user = user
        return root
    }

    override fun onPause() {
        super.onPause()
        if (!infoChanged)
            return

        user?.let {
            DGNetwork.getBaseService().updateUser(it.login, it).enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "Unable to update user ${it.login}: ${t.message}")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful)
                        Log.i(TAG, "User ${it.login} has been successfully updated")
                }
            })
        }

        val firebaseUser = MainApplication.getAuth().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(user?.login)
            .setPhotoUri(Uri.parse(user?.photo))
            .build()
        firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful)
                Log.i(TAG, String.format("Data of user %s have successfully been saved to firebase auth", user?.login))
            else
                Log.e(TAG, "Unable to save user data to firebase auth: ${task.exception?.message}")
        }

        infoChanged = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = CropImage.getActivityResult(data).uri
            val istream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(istream)

            val storage = MainApplication.getStorage()
            val imageRef = storage.getReferenceFromUrl(MainApplication.STORAGE_URL).child("${MainApplication.STORAGE_PROFILE_IMAGES_DIR}/${user?.login}.jpg")
            val uploadTask = imageRef.putBytes(Utils.bitmapToBytes(selectedImage))
            uploadTask.addOnFailureListener { error ->
                Log.e(TAG, "Failed to upload image for user ${user?.login}: ${error.message}")
                Toast.makeText(activity, R.string.profile_image_upload_failure, Toast.LENGTH_LONG).show()
            }.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "Image for user ${user?.login} has not been uploaded")
                    Toast.makeText(activity, R.string.profile_image_upload_failure, Toast.LENGTH_LONG).show()
                    return@addOnCompleteListener
                }

                Log.i(TAG, "Image for user ${user?.login} successfully uploaded")
                imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                    user?.photo = uriTask.result.toString()
                    profile_image_backdrop.setImageBitmap(selectedImage)
                    (activity as MainActivity).updateNavHeaderPhoto()
                    infoChanged = true
                }
            }
        }
    }

    private fun editBirthday() {
        val currBirthday = user?.birthday
        val cal = Calendar.getInstance()
        if (currBirthday != null && currBirthday.isNotEmpty()) {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            cal.time = sdf.parse(currBirthday)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val birthday = getString(R.string.date_format, dayOfMonth, monthOfYear+1, year)
            if (birthday != user?.birthday) {
                user?.birthday = birthday
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
        val gender = user?.gender
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.gender_dialog_title)
        val values = resources.getStringArray(R.array.gender)
        val checkedItem = if (gender != null && gender.isNotEmpty()) values.indexOf(gender) else -1

        builder.setSingleChoiceItems(values, checkedItem) { dialog, item ->
            user?.gender = values[item]
            profile_gender.text = values[item]
            infoChanged = true
            dialog.dismiss()
        }
        val genderDialog = builder.create()
        genderDialog.show()
    }
}
