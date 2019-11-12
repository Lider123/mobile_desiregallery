package com.example.desiregallery.di.components

import android.app.Activity
import android.content.Context
import com.example.desiregallery.MainApplication
import com.example.desiregallery.di.modules.*
import com.example.desiregallery.ui.screens.LoginActivity
import com.example.desiregallery.ui.screens.MainActivity
import com.example.desiregallery.ui.screens.SignUpActivity
import com.example.desiregallery.ui.screens.SplashScreenActivity
import dagger.Component

/**
 * @author Konstantin on 11.11.2019
 */
@Component(modules = [
    ApplicationModule::class,
    AuthModule::class,
    DataModule::class,
    NetworkModule::class,
    PostsModule::class,
    ProfileModule::class
])
interface ApplicationComponent {
    fun inject(app: MainApplication)
    fun inject(activity: SplashScreenActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: SignUpActivity)
}