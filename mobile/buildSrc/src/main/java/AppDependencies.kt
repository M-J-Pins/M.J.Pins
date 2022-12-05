import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    //android ui
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    private const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    private const val composeUIUtil = "androidx.compose.ui:ui-util:${Versions.compose}"
    private const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    private const val composeMaterialIconsExtended =
        "androidx.compose.material:material-icons-extended:${Versions.compose}"
//    private const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    private const val composeActivity =
        "androidx.activity:activity-compose:${Versions.composeActivity}"
    private const val composeViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
    private const val materialTheme3 = "androidx.compose.material3:material3:${Versions.materialTheme3}"
    private const val mt3class = "androidx.compose.material3:material3-window-size-class:${Versions.materialTheme3}"

    //navigation
    private const val navigation = "androidx.navigation:navigation-compose:${Versions.nav}"
    private const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"

    //Dependency Injection
    private const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    private const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    //test
    private const val junit = "junit:junit:${Versions.junit}"
    private const val extJunit = "androidx.test.ext:junit:${Versions.extJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    private const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"

    //barcode=reader
    private const val barcodeScanning =
        "com.google.mlkit:barcode-scanning:${Versions.barcodeScanning}"

    //camera
    private const val camera = "androidx.camera:camera-camera2:${Versions.camerax}"
    private const val cameraLifeCycle = "androidx.camera:camera-lifecycle:${Versions.camerax}"
    private const val cameraView = "androidx.camera:camera-view:${Versions.cameraView}"
    private const val cameraCore = "androidx.camera:camera-core:${Versions.cameraCore}"

    //accompanist
    private const val permissions =
        "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"
    private const val animatedNavigation = "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"

    //pageing
    private const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    private const val accompanistPager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"

    //retrofit
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private const val retrofitGson = "com.squareup.retrofit2:converter-gson:2.3.0"

    //glide
    private const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    val appLibs = listOf(
        kotlinStdLib,
        coreKtx,
        lifecycle,
        composeUI,
        composeUIUtil,
        composeMaterial,
        composeMaterialIconsExtended,
        composeActivity,
        navigation,
        hilt,
        composeViewModel,
        barcodeScanning,
        camera,
        cameraCore,
        cameraView,
        cameraLifeCycle,
        retrofit,
        retrofitGson,
        hiltNavigation,
        animatedNavigation,
        permissions,
        paging,
        accompanistPager,
        materialTheme3,
        mt3class,
        glide
    )

    val kaptLibs = listOf(hiltCompiler)

    val testLibs = listOf(junit)

    val androidTestLibs = listOf(
        extJunit,
        espressoCore,
        composeJunit
    )

}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach {
        add("kapt", it)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach {
        add("testImplementation", it)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach {
        add("androidTestImplementation", it)
    }
}