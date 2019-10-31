package com.example.desiregallery.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.ui.main.MainActivity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.desiregallery.ui.profile.IProfileContract
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileFragment : Fragment(), IProfileContract.View {

    private val presenter: IProfileContract.Presenter by inject { parametersOf(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(view.context.resources)
        initListeners()
    }

    override fun onPause() {
        super.onPause()
        if (presenter.infoChanged)
            presenter.updateProfile(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = CropImage.getActivityResult(data).uri
            val onFailure = {
                Toast.makeText(activity, R.string.profile_image_upload_failure, Toast.LENGTH_LONG)
                    .show()
            }
            val onComplete = { (activity as MainActivity).updateNavHeaderPhoto() }
            presenter.uploadPhoto(imageUri, activity!!.contentResolver, onFailure, onComplete)
        }
    }

    override fun updateTitle(title: String) {
        profile_toolbar.title = title
    }

    override fun updateGender(gender: String) {
        profile_gender.text = gender
    }

    override fun updateBirthday(birthday: String) {
        profile_birthday.text = birthday
    }

    override fun updatePhoto(photoUrl: String) {
        Picasso.with(activity)
            .load(photoUrl)
            .into(profile_image_backdrop)
    }

    override fun updatePhoto(bitmap: Bitmap) {
        profile_image_backdrop.setImageBitmap(bitmap)
    }

    override fun updatePhotoFabVisibility(visible: Boolean) {
        if (visible)
            profile_fab.show()
        else
            profile_fab.hide()
    }

    private fun initListeners() {
        profile_fab.setOnClickListener { CropImage.activity().start(requireContext(), this) }
        profile_gender.setOnClickListener { presenter.onGenderClick(profile_gender.context) }
        profile_birthday.setOnClickListener { presenter.onBirthdayClick(profile_birthday.context) }
    }
}
