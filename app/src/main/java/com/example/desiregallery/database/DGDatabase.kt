package com.example.desiregallery.database

import android.util.Log
import com.example.desiregallery.models.User
import io.realm.Realm
import io.realm.RealmConfiguration


class DGDatabase {
    companion object {
        private val TAG = DGDatabase::class.java.simpleName

        private fun getRealm(): Realm {
            val config = RealmConfiguration.Builder()
                .name("desiregallery.realm")
                .schemaVersion(42)
                .build()
            return Realm.getInstance(config)
        }

        fun updateUser(user: User) {
            val realm = getRealm()
            realm.executeTransactionAsync({
                it.copyToRealmOrUpdate(user)
            }, {
                Log.i(TAG, String.format("User %s has been added to the database", user.getLogin()))
            }, {
                Log.e(TAG, String.format("Error when adding user %s to the database", user.getLogin()))
                it.printStackTrace()
            })
        }

        fun getUser(login: String): User? {
            return getRealm().where(User::class.java).equalTo("login", login).findFirst()
        }
    }
}