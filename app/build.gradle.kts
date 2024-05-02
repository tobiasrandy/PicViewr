plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.app.picviewr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.picviewr"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))

    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.rxjava)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.rxandroid)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}

kapt {
    correctErrorTypes = true
}