buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Kotlin.GRADLE)
        classpath(Dependencies.Kotlin.KOTLIN_GRADLE)
        classpath(Dependencies.Hilt.HILT_PLUGIN)
        classpath(Dependencies.Firebase.GOOGLE_SERVICE)
        classpath(Dependencies.Firebase.FIREBASE_CRASHLYTICS)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}