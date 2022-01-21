plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "app.threedollars.manager"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "0.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        base.archivesBaseName = "3dollar_${versionName}"
    }
    buildFeatures {
        compose = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.5"
    }
}

dependencies {
    implementation(Dependency.Kotlin.KOTLIN)
    implementation(Dependency.Android.CORE_KTX)
    implementation(Dependency.Android.ANDROIDX_APPCOMPAT)
    implementation(Dependency.Android.ANDROID_MATERIAL)
    implementation(Dependency.Android.CONSTRAINT_LAYOUT)
    implementation(Dependency.Compose.COMPOSE_ACTIVITY)
    implementation(Dependency.Compose.COMPOSE_FOUNDATION)
    implementation(Dependency.Compose.COMPOSE_MATERIAL)
    implementation(Dependency.Compose.COMPOSE_ANIMATION)
    implementation(Dependency.Compose.COMPOSE_UI_TOOL)
    implementation(Dependency.Compose.COMPOSE_VIEWMODEL)
    implementation(Dependency.Test.JUNIT)
    androidTestImplementation(Dependency.Test.ANDROIDX_JUNIT)
    androidTestImplementation(Dependency.Test.ANDROIDX_ESPRESSO)
}