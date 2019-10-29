package com.example.desiregallery.analytics

import android.content.Context
import android.os.Bundle
import com.example.desiregallery.auth.AuthMethod
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * @author babaetskv on 18.09.19
 */
class AnalyticsTracker(context: Context) : IDGAnalyticsTracker {
    private val analytics = FirebaseAnalytics.getInstance(context)

    override fun trackLogin(method: AuthMethod) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD, method.name)
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }

    override fun trackSignUp(method: AuthMethod) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD, method.name)
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
    }

    override fun trackSharePhoto(itemId: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post_image")
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        analytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
    }
}