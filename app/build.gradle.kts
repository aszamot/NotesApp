plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinKapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs.kotlin)
}

android {
    namespace = "pl.atk.notes"
    compileSdk = 34

    defaultConfig {
        applicationId = "pl.atk.notes"
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
        buildConfig = true
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
    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.core.ktx)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.mockito.kotlin)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    kapt(libs.androidx.lifecycle.compiler)
    testImplementation(libs.androidx.lifecycle.runtime.testing)

    implementation(libs.kotlinx.coroutines.android)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    androidTestImplementation(libs.androidx.navigation.navigation.testing)

    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    implementation(libs.timber)

    testImplementation(libs.app.turbine)
    androidTestImplementation(libs.app.turbine)
}

kapt {
    correctErrorTypes = true
}