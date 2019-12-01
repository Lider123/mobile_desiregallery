package com.example.desiregallery.di

import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.di.modules.DataModule
import io.mockk.mockk

/**
 * @author babaetskv on 09.12.19
 */
class TestDataModule : DataModule(mockk()) {

    override fun providePrefsHelper(): IDGSharedPreferencesHelper = mockk()

    override fun provideStorageHelper(): IStorageHelper = mockk()
}