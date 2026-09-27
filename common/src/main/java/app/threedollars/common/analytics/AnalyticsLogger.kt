package app.threedollars.common.analytics

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

object AnalyticsLogger {
    fun logScreenView(context: Context, screenName: String) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
    }

    fun logEvent(context: Context, eventName: String, params: Map<String, String> = emptyMap()) {
        FirebaseAnalytics.getInstance(context).logEvent(eventName) {
            params.forEach { (key, value) -> param(key, value) }
        }
    }
}

@Composable
fun ScreenViewLogEffect(screenName: String) {
    val context = LocalContext.current
    LaunchedEffect(screenName) {
        AnalyticsLogger.logScreenView(context, screenName)
    }
}
