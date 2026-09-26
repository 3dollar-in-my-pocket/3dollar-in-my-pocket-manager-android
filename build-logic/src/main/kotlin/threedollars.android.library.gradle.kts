import app.threedollars.manager.configureCoroutineAndroid
import app.threedollars.manager.configureHiltAndroid
import app.threedollars.manager.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()