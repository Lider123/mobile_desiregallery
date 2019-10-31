package com.example.desiregallery.di

import android.content.res.Resources
import android.view.View
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.models.Post
import com.example.desiregallery.presenters.ProfilePresenter
import com.example.desiregallery.sharedprefs.IDGSharedPreferencesHelper
import com.example.desiregallery.sharedprefs.PreferencesHelper
import com.example.desiregallery.ui.contracts.IProfileContract
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.viewmodels.PostListViewModel
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
    single<IDGSharedPreferencesHelper> { PreferencesHelper(androidContext()) }
    single<IDGAnalyticsTracker> { AnalyticsTracker(androidContext()) }
    single { AccountProvider() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }
    single<Resources> { androidContext().resources }
    factory { (container: View) -> SnackbarWrapper(container) }
    factory { (posts: MutableList<Post>) -> PostAdapter(posts) }
    factory<IProfileContract.Presenter> { (view: IProfileContract.View) -> ProfilePresenter(view, get(), get(), get()) }
}

val viewModelModule = module {
    viewModel<PostListViewModel>()
}
