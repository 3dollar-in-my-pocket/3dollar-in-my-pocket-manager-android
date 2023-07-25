import Dependencies.common

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    // id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33
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
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }
    namespace = "app.threedollars.common"
}

dependencies {
    common()
}