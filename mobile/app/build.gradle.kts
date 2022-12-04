plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")

}
apply {
    plugin("kotlin-android")
    plugin("kotlin-android-extensions")
}

android {

    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = Config.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation(files("C:\\Users\\Maxim\\Documents\\accompanist-systemuicontroller-0.28.0.aar"))
    runtimeOnly("dev.chrisbanes.snapper:snapper:0.3.0")
    runtimeOnly("com.google.android.material:material:1.6.1")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AppDependencies.appLibs)
    kapt(AppDependencies.kaptLibs)
    testImplementation(AppDependencies.testLibs)
    androidTestImplementation(AppDependencies.androidTestLibs)


}
repositories {
    mavenCentral()
    google()
    maven { url = uri("https://jitpack.io") }
}