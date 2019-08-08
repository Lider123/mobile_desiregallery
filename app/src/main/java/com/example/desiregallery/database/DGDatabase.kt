package com.example.desiregallery.database

import io.realm.Realm
import io.realm.RealmConfiguration


object DGDatabase {
    private val TAG = DGDatabase::class.java.simpleName

    private fun getRealm(): Realm {
        val config = RealmConfiguration.Builder()
            .name("desiregallery.realm")
            .schemaVersion(42)
            .build()
        return Realm.getInstance(config)
    }
}