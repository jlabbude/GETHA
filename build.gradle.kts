// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.3" apply false
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        //noinspection UseTomlInstead
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.3")
    }
}