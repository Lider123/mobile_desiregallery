package com.example.desiregallery.di.modules

import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.data.storage.StorageHelper
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Konstantin on 11.11.2019
 */
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideStorageHelper(): IStorageHelper {
        val storage = FirebaseStorage.getInstance()
        return StorageHelper(storage)
    }
}