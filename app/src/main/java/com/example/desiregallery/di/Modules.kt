package com.example.desiregallery.di

import android.content.res.Resources
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkHelper
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.data.prefs.PreferencesHelper
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.data.storage.StorageHelper
import com.example.desiregallery.ui.comments.CommentListViewModel
import com.example.desiregallery.ui.feed.IPostContract
import com.example.desiregallery.ui.feed.PostListViewModel
import com.example.desiregallery.ui.feed.PostPresenter
import com.example.desiregallery.ui.profile.IProfileContract
import com.example.desiregallery.ui.profile.ProfilePresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author babaetskv on 29.10.19
 */
val applicationModule = module(override = true) {
    single<Resources> { androidContext().resources }
    single<IDGSharedPreferencesHelper> { PreferencesHelper(androidContext()) }
    single<IDGAnalyticsTracker> { AnalyticsTracker(androidContext()) }
    single<IStorageHelper> { StorageHelper(get()) }
    single { AccountProvider() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }

    factory<IProfileContract.Presenter> { (view: IProfileContract.View) -> ProfilePresenter(view) }
    factory<IPostContract.Presenter> { (view: IPostContract.View, post: Post) -> PostPresenter(view, post) }
}

val networkModule = module {
    single { QueryNetworkService.getService() }
    single { BaseNetworkService.createService() }
    single { NetworkHelper() }
}

val viewModelModule = module {
    viewModel { PostListViewModel(get(), get()) }
    viewModel { (postId: String) -> CommentListViewModel(get(), postId, get()) }
}
