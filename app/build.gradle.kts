plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.ufpb.getha"
    compileSdk = 35

    buildFeatures {
        compose = true
    }

 androidResources

    defaultConfig {
        applicationId = "com.ufpb.getha"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    androidResources {
        noCompress += listOf("pdf")
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.media3.ui)
    implementation(libs.navigation.ui)
    //noinspection UseTomlInstead
    implementation("androidx.core:core-ktx:1.13.1")
    implementation(libs.media3.exoplayer)
    implementation(libs.lifecycle.viewmodel.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.mediassesion)
    implementation(libs.design)
    implementation(libs.ui)
    implementation(libs.foundation)
    implementation(libs.tooling)
    //noinspection UseTomlInstead
    implementation("io.ktor:ktor-client-core:2.3.12")
    //noinspection UseTomlInstead
    implementation("io.ktor:ktor-client-android:2.3.12")
    //noinspection UseTomlInstead
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3:1.3.0")
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3-window-size-class:1.3.0")
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha02")
    //noinspection UseTomlInstead
    implementation("androidx.navigation:navigation-compose:2.8.3")
    //noinspection UseTomlInstead
    implementation("androidx.compose.material:material-icons-core:1.7.4")
    //noinspection UseTomlInstead
    implementation("io.sanghun:compose-video:1.2.0")
    //noinspection UseTomlInstead
    implementation("androidx.media3:media3-session:1.4.1")
    //noinspection UseTomlInstead
    implementation("androidx.media3:media3-exoplayer-dash:1.4.1")
    //noinspection UseTomlInstead
    implementation("androidx.media3:media3-exoplayer-hls:1.4.1")
}