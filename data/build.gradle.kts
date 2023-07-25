import Dependencies.common

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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
            buildConfigField("String", "KAKAO_KEY", com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)["kakao_key_release"] as? String ?: "")
            buildConfigField("String", "BASE_URL", com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)["base_url_release"] as? String ?: "")
        }

        getByName("debug") {
            buildConfigField("String", "KAKAO_KEY", com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)["kakao_key_dev"] as? String ?: "")
            buildConfigField("String", "BASE_URL", com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)["base_url_dev"] as? String ?: "")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }
    namespace = "app.threedollars.data"
}

dependencies {
    common()
    implementation(project(":common"))
    implementation(project(":domain"))
}