import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    //ktor
    private const val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    private const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    private const val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    private const val ktorResource = "io.ktor:ktor-client-resources:${Versions.ktor}"
    private const val ktorNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    private const val ktorJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"

    //android ui
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    private const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    private const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    private const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    private const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    private const val composeActivity = "androidx.activity:activity-compose:${Versions.composeActivity}"
    private const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"

    //navigation
    private const val navigation = "androidx.navigation:navigation-compose:${Versions.nav}"

    //serialization
    private const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"

    //Dependency Injection
    private const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    private const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    //test
    private const val junit = "junit:junit:${Versions.junit}"
    private const val extJunit = "androidx.test.ext:junit:${Versions.extJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    private const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"

    //barcode=reader
    private const val barcodeScanning = "com.google.mlkit:barcode-scanning:${Versions.barcodeScanning}"
    //camera
    private const val camera = "androidx.camera:camera-camera2:${Versions.camerax}"
    private const val cameraLifeCycle = "androidx.camera:camera-lifecycle:${Versions.camerax}"
    private const val cameraView = "androidx.camera:camera-view:${Versions.cameraView}"
    private const val cameraCore = "androidx.camera:camera-core:${Versions.cameraCore}"
    private const val cameraPermission = "com.google.accompanist:accompanist-permissions:${Versions.cameraPermission}"

    val appLibs = listOf(
        kotlinStdLib,
        coreKtx,
        lifecycle,
        composeUI,
        composeMaterial,
        composeMaterialIconsExtended,
        composePreview,
        composeActivity,
        navigation,
        serialization,
        hilt,
        ktorCore,
        ktorAndroid,
        ktorLogging,
        ktorResource,
        ktorNegotiation,
        composeViewModel,
        ktorJson,
        barcodeScanning,
        camera,
        cameraCore,
        cameraView,
        cameraLifeCycle,
        cameraPermission
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
    list.forEach{
        add("implementation", it)
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach{
        add("kapt", it)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach{
        add("testImplementation", it)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach{
        add("androidTestImplementation", it)
    }
}