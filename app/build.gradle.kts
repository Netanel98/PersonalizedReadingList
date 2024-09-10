import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 34
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
        dataBinding = true
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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx.v261)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v261)
    implementation(libs.androidx.fragment.ktx.v156)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx.v261)
    implementation(libs.lifecycle.viewmodel.ktx.v261)
    implementation(libs.fragment.ktx.v156)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.glide)
    implementation(libs.room.runtime)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation(libs.androidx.room.ktx)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.navigation.fragment.ktx.vlasterversion)
    implementation(libs.androidx.navigation.ui.ktx.vlasterversion)
    implementation(libs.androidx.navigation.fragment.ktx.v253)
    implementation(libs.androidx.navigation.ui.ktx.v253)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
}

kapt {
    correctErrorTypes = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}