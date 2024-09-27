import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id ("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.nimbusweatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nimbusweatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        val localProperty = Properties()
        val file = rootProject.file("local.properties")
        if(file.exists())
        {
            file.inputStream().use {
                localProperty.load(it)
            }
        }

        val apiKey : String = localProperty.getProperty("API_KEY") ?: "null"


        buildConfigField("String","API_KEY",apiKey)

    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
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
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //testing
    testImplementation("app.cash.turbine:turbine:0.7.0")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")



////more features in the activity like lazy initialization
    implementation(libs.androidx.activity.ktx)
////to use viewModel with fragment
    implementation(libs.androidx.fragment.ktx)

    //retrofit and gson converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    ///glide dependency for images
    implementation(libs.glide)



///room database
    val room_version = "2.6.1"

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)


    ///open street map api
    implementation(libs.osmdroid.android)

    ///hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    ///navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    //lottie animation
    implementation(libs.lottie)
}