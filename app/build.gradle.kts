plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.lab9"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lab9"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.kotlin.stdlib)

    implementation ("androidx.constraintlayout:constraintlayout:2.2.0")

    // RecyclerView
    implementation (libs.androidx.recyclerview)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)

    // Lifecycle components
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)

    // OkHttp Logging Interceptor (для відлагодження)
    implementation (libs.logging.interceptor)

    // Connectivity Manager
    implementation (libs.androidx.lifecycle.runtime.ktx)

    // Testing
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit.v115)
    androidTestImplementation (libs.androidx.espresso.core.v351)
}