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
                Log.i(TAG, String.format("User %s has been updated in database", user.getLogin()))
            }, {
                Log.e(TAG, String.format("Error when updating user %s in database", user.getLogin()))
                it.printStackTrace()
            })
        }

        fun createUser(user: User) {
            val realm = getRealm()
            realm.executeTransaction {
                it.copyToRealmOrUpdate(user)
                Log.i(TAG, String.format("User %s has been added to the database", user.getLogin()))
            }
        }

        fun getUser(login: String): User? {
            val user = getRealm().where(User::class.java).equalTo("login", login).findFirst()
            return if (user != null) getRealm().copyFromRealm(user) else null
        }
    }
}