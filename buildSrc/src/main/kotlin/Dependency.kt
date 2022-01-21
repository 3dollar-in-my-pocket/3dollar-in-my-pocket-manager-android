object Dependency {
    object Kotlin {
        const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    }

    object Android {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"
        const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"
        const val CONSTRAINT_LAYOUT = "androidx.constraint's:constraint's:${Versions.CONSTRAINT_LAYOUT}"
    }

    object Compose {
        const val COMPOSE_ACTIVITY = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
        const val COMPOSE_MATERIAL = "androidx.activity:activity-compose:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_ANIMATION = "androidx.activity:activity-compose:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_UI_TOOL = "androidx.activity:activity-compose:${Versions.COMPOSE_MATERIAL}"
        const val COMPOSE_VIEWMODEL = "androidx.activity:activity-compose:${Versions.COMPOSE_LIFECYCLE_VIEWMODEL}"
        const val COMPOSE_UI_TEST = "androidx.activity:activity-compose:${Versions.COMPOSE_MATERIAL}"
    }

    object Test {
        const val JUNIT = "junit:junit:"
        const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_JUNIT}"
        const val ANDROIDX_ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ANDROIDX_ESPRESSO}"
    }
}