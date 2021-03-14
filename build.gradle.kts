// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath( "com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://oss.jfrog.org/libs-snapshot")
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
