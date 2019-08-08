package com.example.desiregallery.ui.dialogs

import androidx.appcompat.app.AlertDialog
import com.example.desiregallery.R
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.desiregallery.database.DGDatabase
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import com.example.desiregallery.ui.activities.MainActivity
import kotlinx.android.synthetic.main.dialog_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordDialog(private val activity: Activity) : AlertDialog(activity) {
    companion object {
        private val TAG = ChangePasswordDialog::class.java.simpleName
    }

    private var currentPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val content = View.inflate(context, R.layout.dialog_change_password, null)
        setView(content)
        setTitle(R.string.change_password)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.OK)) { _, _ -> handleConfirm() }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.Cancel)) { _, _ -> handleCancel() }
        setCancelable(false)

        currentPassword = (activity as MainActivity).getCurrUser()?.password

        super.onCreate(savedInstanceState)
    }

    private fun handleConfirm() {
        if (dialog_password_current.text.isEmpty()
            || dialog_password_new.text.isEmpty()
            || dialog_password_confirm.text.isEmpty()) {
            Toast.makeText(activity, R.string.fields_are_empty, Toast.LENGTH_LONG).show()
            return
        }
        if (dialog_password_current.text.toString() != currentPassword) {
            Toast.makeText(activity, R.string.current_password_wrong, Toast.LENGTH_LONG).show()
            return
        }
        if (dialog_password_new.text.toString() != dialog_password_confirm.text.toString()) {
            Toast.makeText(activity, R.string.non_equal_passwords, Toast.LENGTH_LONG).show()
            return
        }
        updatePassword()
        Toast.makeText(activity, R.string.password_changed, Toast.LENGTH_LONG).show()
        dismiss()
    }

    private fun updatePassword() {
        val user = (activity as MainActivity).getCurrUser()
        user?.let {
            it.password = dialog_password_new.text.toString()
            DGDatabase.updateUser(it)
            DGNetwork.getService().updateUser(it.getLogin(), it).enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "Unable to update user ${it.getLogin()}: ${t.message}")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful)
                        Log.i(TAG, "User ${it.getLogin()} has been successfully updated")
                }
            })
        }
    }

    private fun handleCancel() = dismiss()
}