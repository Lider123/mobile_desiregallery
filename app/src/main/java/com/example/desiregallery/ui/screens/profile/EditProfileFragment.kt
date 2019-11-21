package com.example.desiregallery.ui.screens.profile

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author babaetskv on 06.11.19
 */
class EditProfileFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var storageHelper: IStorageHelper
    @Inject
    lateinit var networkManager: NetworkManager

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    private lateinit var user: User
    private var callback: Callback? = null
    private var newImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        arguments?.let {
            user = it.getSerializable(EXTRA_USER) as User
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user.photo.isEmpty())
            Picasso.with(requireContext())
                .load(R.drawable.material)
                .resize(200, 200)
                .into(edit_photo)
        else
            Picasso.with(requireContext())
                .load(user.photo)
                .resize(200, 200)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.material)
                .into(edit_photo)
        edit_birthday.setText(user.birthday)

        initListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            newImageUri = CropImage.getActivityResult(data).uri
            Picasso.with(requireContext())
                .load(newImageUri)
                .resize(200, 200)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.material)
                .into(edit_photo)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }

    private fun initListeners() {
        edit_birthday.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                user.birthday = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        edit_birthday.setOnClickListener {
            setErrorMessageVisibility(false)
            editBirthday()
        }
        edit_new_image.setOnClickListener {
            setErrorMessageVisibility(false)
            CropImage.activity().start(requireContext(), this)
        }
        edit_done.setOnClickListener {
            setErrorMessageVisibility(false)
            showProgress()
            val finishEditing = {
                updateProfile()
                callback?.onDoneClick(user)
                hideProgress()
            }
            newImageUri?.let {
                uploadPhoto(it,
                    {
                        setErrorMessageVisibility(true)
                        hideProgress()
                    },
                    finishEditing)
            }?: finishEditing()
        }
        edit_cancel.setOnClickListener {
            setErrorMessageVisibility(false)
            callback?.onCancelClick()
        }
    }

    private fun uploadPhoto(uri: Uri, onFailure: () -> Unit, onComplete: () -> Unit) {
        val resolver = requireActivity().contentResolver
        val istream = resolver.openInputStream(uri)
        val selectedImage = BitmapFactory.decodeStream(istream)
        coroutineScope.launch(Dispatchers.Main) {
            when (val result = storageHelper.uploadProfileImage(selectedImage, user.login)) {
                is Result.Success -> {
                    logInfo(TAG, "Image for user ${user.login} successfully uploaded")
                    user.photo = result.data
                    onComplete()
                }
                is Result.Error -> {
                    logError(TAG, "Failed to upload image for user ${user.login}: ${result.exception}")
                    onFailure()
                }
            }
        }
    }

    private fun editBirthday() {
        val currBirthday = user.birthday
        val cal = Calendar.getInstance()
        if (currBirthday.isNotEmpty()) {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            cal.time = sdf.parse(currBirthday)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val birthday = requireContext().getString(R.string.date_format, dayOfMonth, monthOfYear+1, year)
            if (birthday != user.birthday)
                edit_birthday.setText(birthday)
        }
        DatePickerDialog(requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateProfile() {
        coroutineScope.launch(Dispatchers.Main) {
            when (val result = networkManager.updateUser(user)) {
                is Result.Success -> logInfo(TAG, "User ${user.login} successfully updated")
                is Result.Error -> logError(TAG, result.exception.message ?: "Failed to update user ${user.login}")
            }
        }

        val firebaseUser = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(user.login)
            .setPhotoUri(Uri.parse(user.photo))
            .build()
        firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful)
                logInfo(TAG, "Data of user ${user.login} have successfully been saved to firebase auth")
            else
                logError(TAG, "Unable to save user data to firebase auth: ${task.exception?.message}")
        }
    }

    private fun showProgress() {
        edit_progress.visibility = View.VISIBLE
        edit_done.isEnabled = false
        edit_cancel.isEnabled = false
        edit_birthday.isEnabled = false
        edit_new_image.isEnabled = false
    }

    private fun hideProgress() {
        edit_progress.visibility = View.GONE
        edit_done.isEnabled = true
        edit_cancel.isEnabled = true
        edit_birthday.isEnabled = true
        edit_new_image.isEnabled = true
    }

    private fun setErrorMessageVisibility(visible: Boolean) {
        edit_error.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setCallback(callback: Callback) {
        this.callback = callback
    }

    companion object {
        private val TAG = EditProfileFragment::class.java.simpleName
        private const val EXTRA_USER = "user"

        fun createInstance(user: User, callback: Callback): EditProfileFragment {
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_USER, user)
            val instance = EditProfileFragment()
            instance.arguments = bundle
            instance.setCallback(callback)
            return instance
        }
    }

    interface Callback {
        fun onDoneClick(user: User)
        fun onCancelClick()
    }
}
