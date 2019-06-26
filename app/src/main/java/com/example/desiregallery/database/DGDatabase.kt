package com.example.desiregallery.database

import android.util.Log
import com.example.desiregallery.models.User
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Class that provides functions to operate with the database
 *
 * @author babaetskv
 * */
class DGDatabase {
    companion object {
        private val TAG = DGDatabase::class.java.simpleName

        /**
         * Method that provides Realm instance to manage database
         *
         * @return Instance of Realm
         * */
        private fun getRealm(): Realm {
            val config = RealmConfiguration.Builder()
                .name("desiregallery.realm")
                .schemaVersion(42)
                .build()
            return Realm.getInstance(config)
        }

        /**
         * Method that updates user in the database
         *
         * @param user User data to update
         * */
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

        /**
         * Method that creates user in the database
         *
         * @param user User data for adding
         * */
        fun createUser(user: User) {
            val realm = getRealm()
            realm.executeTransaction {
                it.copyToRealmOrUpdate(user)
                Log.i(TAG, String.format("User %s has been added to the database", user.getLogin()))
            }
        }

        /**
         * Method for getting user from the database
         *
         * @param login Login that identifies user
         * @return User data if database contains user with that login, null otherwise
         * */
        fun getUser(login: String): User? {
            val user = getRealm().where(User::class.java).equalTo("login", login).findFirst()
            return if (user != null) getRealm().copyFromRealm(user) else null
        }
    }
}