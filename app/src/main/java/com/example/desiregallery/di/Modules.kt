package com.example.desiregallery.di

import android.content.res.Resources
import android.view.View
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.ui.profile.ProfilePresenter
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.data.prefs.PreferencesHelper
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.data.storage.StorageHelper
import com.example.desiregallery.ui.profile.IProfileContract
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.ui.feed.PostListViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module

/**
 * @author babaetskv on 29.10.19
 */
val applicationModule = module(override = true) {
    single<Resources> { androidContext().resources }
    single<IDGSharedPreferencesHelper> { PreferencesHelper(androidContext()) }
    single<IDGAnalyticsTracker> { AnalyticsTracker(androidContext()) }
    single<IStorageHelper> { StorageHelper(get()) }
    factory { (container: View) -> SnackbarWrapper(container) }
    single { AccountProvider() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }
    factory<IProfileContract.Presenter> { (view: IProfileContract.View) ->
        ProfilePresenter(view, get(), get(), get())
    }
}

val viewModelModule = module {
    viewModel<PostListViewModel>()
}
