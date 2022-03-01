import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    object Kotlin {
        const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        const val GRADLE = "com.android.tools.build:gradle:${Versions.GRADLE}"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    }

    object Android {
        const val DESUGAR_LIBS = "com.android.tools:desugar_jdk_libs:${Versions.DESUGAR_LIBS}"
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"
        const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    }

    object Firebase {
        const val GOOGLE_SERVICE = "com.google.gms:google-services:${Versions.GOOGLE_SERVICE}"
        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics"
        const val FIREBASE_ANALYTICS_KTX = "com.google.firebase:firebase-analytics-ktx"
        const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS}"
        const val FIREBASE_CRASHLYTICS_KTX = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object Compose {
        const val COMPOSE_ACTIVITY = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
        const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_MATERIAL = "androidx.compose.material:material:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_ANIMATION = "androidx.compose.animation:animation:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_UI_TOOL = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.COMPOSE_LIFECYCLE_VIEWMODEL}"
    }

    object Hilt {
        const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
        const val HILT_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
        const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_JUNIT}"
        const val ANDROIDX_ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_ESPRESSO}"
    }

    fun DependencyHandler.common() {
        coreLibraryDesugaring(Android.DESUGAR_LIBS)
        implementation(platform(Firebase.FIREBASE_BOM))
        implementation(Firebase.FIREBASE_ANALYTICS)
        implementation(Firebase.FIREBASE_ANALYTICS_KTX)
        implementation(Firebase.FIREBASE_CRASHLYTICS_KTX)
        implementation(Kotlin.KOTLIN)
        implementation(Android.CORE_KTX)
        implementation(Android.ANDROIDX_APPCOMPAT)
        implementation(Android.ANDROID_MATERIAL)
        implementation(Android.CONSTRAINT_LAYOUT)
        implementation(Compose.COMPOSE_ACTIVITY)
        implementation(Compose.COMPOSE_FOUNDATION)
        implementation(Compose.COMPOSE_MATERIAL)
        implementation(Compose.COMPOSE_ANIMATION)
        implementation(Compose.COMPOSE_UI_TOOL)
        implementation(Compose.COMPOSE_VIEWMODEL)
        kapt(Hilt.HILT_COMPILER)
        implementation(Hilt.HILT_ANDROID)
        implementation(Test.JUNIT)
        androidTestImplementation(Test.ANDROIDX_JUNIT)
        androidTestImplementation(Test.ANDROIDX_ESPRESSO)
    }

    fun DependencyHandler.implementation(dependency: Any) {
        add("implementation", dependency)
    }

    fun DependencyHandler.kapt(dependency: String) {
        add("kapt", dependency)
    }

    fun DependencyHandler.testImplementation(dependency: String) {
        add("testImplementation", dependency)
    }

    fun DependencyHandler.androidTestImplementation(dependency: String) {
        add("androidTestImplementation", dependency)
    }

    fun DependencyHandler.api(dependency: String) {
        add("api", dependency)
    }

    fun DependencyHandler.coreLibraryDesugaring(dependency: String) {
        add("coreLibraryDesugaring", dependency)
    }
}