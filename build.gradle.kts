buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependency.Kotlin.GRADLE)
        classpath(Dependency.Kotlin.KOTLIN_GRADLE)
        classpath(Dependency.Hilt.HILT_PLUGIN)

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