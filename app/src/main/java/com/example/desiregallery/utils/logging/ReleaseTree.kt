package com.example.desiregallery.utils.logging

import android.util.Log
import com.crashlytics.android.Crashlytics
import io.sentry.Sentry
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import timber.log.Timber

/**
 * @author babaetskv on 21.11.19
 */
class ReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.WARN -> {
                Crashlytics.log(priority, tag, message)
                EventBuilder()
                    .withLevel(Event.Level.WARNING)
                    .withTag("tag", tag)
                    .withMessage(message)
                    .build().let {
                        Sentry.capture(it)
                    }
            }
            Log.ERROR -> {
                Crashlytics.log(priority, tag, message)
                EventBuilder()
                    .withLevel(Event.Level.ERROR)
                    .withTag("tag", tag)
                    .withMessage(message)
                    .build().let {
                        Sentry.capture(it)
                    }
            }
        }
    }
}
