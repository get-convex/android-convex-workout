import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "1.9.0"
}

val workoutPropertiesFile = rootProject.file("workout.properties")
val workoutProperties = Properties()
workoutProperties.load(FileInputStream(workoutPropertiesFile))

android {
    namespace = "dev.convex.workouttracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.convex.workouttracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "convex_url", workoutProperties.getProperty("convex.prod_url"))
            resValue("string", "com_auth0_scheme", workoutProperties.getProperty("auth0.scheme"))
            resValue(
                "string",
                "com_auth0_client_id",
                workoutProperties.getProperty("auth0.prod_client_id")
            )
            resValue(
                "string",
                "com_auth0_domain",
                workoutProperties.getProperty("auth0.prod_domain")
            )

            manifestPlaceholders["auth0Domain"] = workoutProperties.getProperty("auth0.prod_domain")
            manifestPlaceholders["auth0Scheme"] = workoutProperties.getProperty("auth0.scheme")
        }

        debug {
            resValue("string", "convex_url", workoutProperties.getProperty("convex.dev_url"))
            resValue("string", "com_auth0_scheme", workoutProperties.getProperty("auth0.scheme"))
            resValue(
                "string",
                "com_auth0_client_id",
                workoutProperties.getProperty("auth0.dev_client_id")
            )
            resValue(
                "string",
                "com_auth0_domain",
                workoutProperties.getProperty("auth0.dev_domain")
            )

            manifestPlaceholders["auth0Domain"] = workoutProperties.getProperty("auth0.dev_domain")
            manifestPlaceholders["auth0Scheme"] = workoutProperties.getProperty("auth0.scheme")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("dev.convex:android-convexmobile:0.4.1@aar") {
        isTransitive = true
    }
    implementation("dev.convex:android-convex-auth0:0.2.1")
    implementation("com.auth0.android:auth0:2.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20")
}