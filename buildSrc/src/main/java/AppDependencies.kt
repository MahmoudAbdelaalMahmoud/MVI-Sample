import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

object AppDependencies {
    private object Androidx {
        //std lib
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

        //android ui
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha05"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
        //test libs
        const val junit = "junit:junit:${Versions.junit}"
        const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Lifecycle {
        private const val version = "2.3.0-beta01"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version" // viewModelScope
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version" // lifecycleScope
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
    }

    object DI{
        const val hilt = "com.google.dagger:hilt-android:${Versions.Hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.Hilt}"

    }
    object ReactiveX {
        private const val version = "3.0.0"
        private const val versionJava = "3.0.11"
        private const val versionKotlin = "3.0.1"
        private const val rxBindingVersion = "4.0.0"

        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:$version"
        const val rxJava = "io.reactivex.rxjava3:rxjava:$versionJava"
        const val rxKotlin = "io.reactivex.rxjava3:rxkotlin:$versionKotlin"


        const val rxBinding =  "com.jakewharton.rxbinding4:rxbinding:$rxBindingVersion"
        const val core =  "com.jakewharton.rxbinding4:rxbinding-core:$rxBindingVersion"
        const val material =  "com.jakewharton.rxbinding4:rxbinding-material:$rxBindingVersion"
        const val appcompat =  "com.jakewharton.rxbinding4:rxbinding-appcompat:$rxBindingVersion"
        const val drawerlayout =  "com.jakewharton.rxbinding4:rxbinding-drawerlayout:$rxBindingVersion"
        const val leanback =  "com.jakewharton.rxbinding4:rxbinding-leanback:$rxBindingVersion"
        const val recyclerview =  "com.jakewharton.rxbinding4:rxbinding-recyclerview:$rxBindingVersion"
        const val slidingPaneLayout =  "com.jakewharton.rxbinding4:rxbinding-slidingpanelayout:$rxBindingVersion"
        const val swipeRefreshLayout =  "com.jakewharton.rxbinding4:rxbinding-swiperefreshlayout:$rxBindingVersion"
        const val viewpager =  "com.jakewharton.rxbinding4:rxbinding-viewpager:$rxBindingVersion"
        const val viewpager2 =  "com.jakewharton.rxbinding4:rxbinding-viewpager2:$rxBindingVersion"
    }
    object SquareUp {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.9.0"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.8.1"
        const val gson = "com.google.code.gson:gson:2.8.5"
    }
    object JetBrains {
        private const val version = "1.4.0"

        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    val appLibraries = arrayListOf<String>().apply {
        add(Androidx.kotlinStdLib)
        add(Androidx.coreKtx)
        add(Androidx.material)
        add(Androidx.appcompat)
        add(Androidx.swipeRefreshLayout)
        add(Androidx.recyclerView)
        add(Androidx.constraintLayout)

        add(ReactiveX.rxJava)
        add(ReactiveX.rxAndroid)
        add(ReactiveX.rxKotlin)

        add(ReactiveX.rxBinding)
        add(ReactiveX.core)
        add(ReactiveX.material)
        add(ReactiveX.appcompat)
        add(ReactiveX.drawerlayout)
        add(ReactiveX.leanback)
        add(ReactiveX.recyclerview)
        add(ReactiveX.slidingPaneLayout)
        add(ReactiveX.swipeRefreshLayout)
        add(ReactiveX.viewpager)
        add(ReactiveX.viewpager2)

        add(Lifecycle.commonJava8)
        add(Lifecycle.runtimeKtx)
        add(Lifecycle.viewModelKtx)

        add(SquareUp.retrofit)
        add(SquareUp.gsonConverter)
        add(SquareUp.loggingInterceptor)
        add(SquareUp.gson)

        add(JetBrains.coroutinesCore)
        add(JetBrains.coroutinesAndroid)

        add(DI.hilt)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(Androidx.extJUnit)
        add(Androidx.espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(Androidx.junit)
    }
}


inline val PluginDependenciesSpec.androidApplication: PluginDependencySpec get() = id("com.android.application")
inline val PluginDependenciesSpec.kotlinAndroid: PluginDependencySpec get() = id("kotlin-android")
inline val PluginDependenciesSpec.kapt: PluginDependencySpec get() = id("kotlin-kapt")
inline val PluginDependenciesSpec.hilt: PluginDependencySpec get() = id("dagger.hilt.android.plugin")

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}