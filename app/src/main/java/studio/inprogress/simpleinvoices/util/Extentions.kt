package studio.inprogress.simpleinvoices.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.CheckResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

fun Context.toast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

@ExperimentalCoroutinesApi
@CheckResult
fun View.clicks(): Flow<View> {
    return callbackFlow {
        setOnClickListener { this.trySend(it).isSuccess }
        awaitClose { setOnClickListener(null) }
    }
}

@ExperimentalCoroutinesApi
fun <T, R> Flow<T>.flatMapFirst(transform: suspend (value: T) -> Flow<R>): Flow<R> =
    map(transform).flattenFirst()

@ExperimentalCoroutinesApi
fun <T> Flow<Flow<T>>.flattenFirst(): Flow<T> = channelFlow {
    val outerScope = this
    val busy = AtomicBoolean(false)
    collect { inner ->
        if (busy.compareAndSet(false, true)) {
            launch {
                try {
                    inner.collect { outerScope.send(it) }
                    busy.set(false)
                } catch (e: CancellationException) {
                    // cancel outer scope on cancellation exception, too
                    outerScope.cancel(e)
                }
            }
        }
    }
}
