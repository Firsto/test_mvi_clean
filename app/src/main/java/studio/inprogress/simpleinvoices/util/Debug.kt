package studio.inprogress.simpleinvoices.util

import studio.inprogress.simpleinvoices.BuildConfig
import timber.log.Timber

object Debug {
    @PublishedApi
    internal inline fun log(block: () -> Unit) {
        if (BuildConfig.DEBUG && Timber.treeCount > 0) block()
    }

    inline fun info(t: Throwable? = null, message: () -> String) =
        log { Timber.i(t, message()) }

    inline fun debug(t: Throwable? = null, message: () -> String) =
        log { Timber.d(t, message()) }

    inline fun warning(t: Throwable? = null, message: () -> String) {
        log { Timber.e(t, message()) }

        if (!BuildConfig.DEBUG) t?.let {
        //FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}
