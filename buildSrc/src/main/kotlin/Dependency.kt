object Dependency {
    object Kotlin {
        const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    }

    object Android {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"
        const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    }

    object Compose {
        const val COMPOSE_ACTIVITY = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
        const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_MATERIAL = "androidx.compose.material:material:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_ANIMATION = "androidx.compose.animation:animation:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_UI_TOOL = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.COMPOSE_LIFECYCLE_VIEWMODEL}"
    }

    object Test {
        const val JUNIT = "junit:junit:"
        const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_JUNIT}"
        const val ANDROIDX_ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_ESPRESSO}"
    }
}