import Dependencies.common
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31
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
            buildConfigField(
                "String",
                "KAKAO_AUTH_KEY",
                gradleLocalProperties(rootDir)["kakao_auth_key"] as? String ?: ""
            )
            buildConfigField(
                "String",
                "BASE_URL",
                gradleLocalProperties(rootDir)["base_url_release"] as? String ?: ""
            )
        }
        getByName("debug") {
            buildConfigField(
                "String",
                "KAKAO_AUTH_KEY",
                gradleLocalProperties(rootDir)["kakao_auth_key"] as? String ?: ""
            )
            buildConfigField(
                "String",
                "BASE_URL",
                gradleLocalProperties(rootDir)["base_url_dev"] as? String ?: ""
            )
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
        kotlinCompilerExtensionVersion = "1.2.0-alpha04"
    }
}

dependencies {
    common()
    implementation(project(":domain"))
}