package app.threedollars.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun CoroutineScope.catchOrCancel(
    crossinline onCatch: suspend CoroutineScope.(Throwable) -> Unit,
    crossinline block: suspend CoroutineScope.() -> Unit,
) {
    try {
        block.invoke(this)
    } catch (e: CancellationException) {
        ensureActive()
    } catch (e: Exception) {
        onCatch(e)
    }
}
