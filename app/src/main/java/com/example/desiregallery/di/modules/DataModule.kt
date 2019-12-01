package com.example.desiregallery.di.modules

import android.content.Context
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.data.prefs.PreferencesHelper
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.data.storage.StorageHelper
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Module
open class DataModule(private val context: Context) {

    @Singleton
    @Provides
    open fun providePrefsHelper(): IDGSharedPreferencesHelper = PreferencesHelper(context)

    @Singleton
    @Provides
    open fun provideStorageHelper(): IStorageHelper = StorageHelper(FirebaseStorage.getInstance())
}
