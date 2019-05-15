package com.example.desiregallery.ui.dialogs

import androidx.appcompat.app.AlertDialog
import com.example.desiregallery.R
import android.view.LayoutInflater
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
    private val TAG = ChangePasswordDialog::class.java.simpleName

    private var currentPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val content = LayoutInflater.from(context).inflate(R.layout.dialog_change_password, null)
        setView(content)
        setTitle(R.string.change_password)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.OK)) { dialog, which ->
            handleConfirm()
        }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.Cancel)) { dialog, which ->
            handleCancel()
        }
        setCancelable(false)

        currentPassword = (activity as MainActivity).getCurrUser()?.getPassword()

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
        user?.setPassword(dialog_password_new.text.toString())
        DGDatabase.updateUser(user!!)
        DGNetwork.getService().updateUser(user.getLogin(), user).enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "Unable to update user")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful)
                    Log.i(TAG, "User has been successfully updated")
            }

        })
    }

    private fun handleCancel() {
        dismiss()
    }
}