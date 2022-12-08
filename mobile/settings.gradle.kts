pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion("1.7.10")
            }
            if (requested.id.id.startsWith("org.jetbrains.kotlin.extensions")) {
                useModule("org.jetbrains.kotlin:kotlin-android-extensions-runtime:1.7.10")
            }
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:7.3.1")
            }
            if (requested.id.id.startsWith("dagger.hilt.android")) {
                useModule("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
            }
            if (requested.id.id.startsWith("com.google.devtools.ksp")) {
                useModule("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
            }
        }
    }
}
include(":app")
rootProject.name = "QuickWallet"
