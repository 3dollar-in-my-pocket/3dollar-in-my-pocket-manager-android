package app.threedollars.manager

import android.app.Application
import android.content.Context
import app.threedollars.manager.BuildConfig.KAKAO_KEY
import com.google.firebase.analytics.FirebaseAnalytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        private lateinit var context: Context

        lateinit var eventTracker: FirebaseAnalytics
            private set

        @JvmStatic
        fun getContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, KAKAO_KEY)
    }
}