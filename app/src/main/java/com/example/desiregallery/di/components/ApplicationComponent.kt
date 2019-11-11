package com.example.desiregallery.di.components

import com.example.desiregallery.MainApplication
import com.example.desiregallery.di.modules.ApplicationModule
import dagger.Component

/**
 * @author Konstantin on 11.11.2019
 */
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(app: MainApplication)
}