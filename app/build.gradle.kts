import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    // alias(libs.plugins.googleServices)  // aktifkan nanti setelah ada google-services.json
}

android {
    namespace = "com.drivemap.app"
    compileSdk = 35

    lint {
        abortOnError = false
        warningsAsErrors = false
        checkReleaseBuilds = false
    }

    defaultConfig {
        applicationId = "com.drivemap.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
            signingConfig = signingConfigs.findByName("release")
        }
        debug { }
    }

    signingConfigs {
        create("release") {
            val ksPath = System.getenv("DRIVEMAP_KEYSTORE_PATH")
                ?: project.findProperty("DRIVEMAP_KEYSTORE_PATH") as String?
            if (!ksPath.isNullOrBlank()) {
                storeFile = file(ksPath)
                storePassword = System.getenv("DRIVEMAP_KEYSTORE_PASSWORD")
                    ?: (project.findProperty("DRIVEMAP_KEYSTORE_PASSWORD") as String?)
                keyAlias = System.getenv("DRIVEMAP_KEY_ALIAS")
                    ?: (project.findProperty("DRIVEMAP_KEY_ALIAS") as String?)
                keyPassword = System.getenv("DRIVEMAP_KEY_ALIAS_PASSWORD")
                    ?: (project.findProperty("DRIVEMAP_KEY_ALIAS_PASSWORD") as String?)
            }
        }
    }

    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlin {
        jvmToolchain(17)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = false
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/DEPENDENCIES","META-INF/LICENSE","META-INF/LICENSE.txt",
            "META-INF/license.txt","META-INF/NOTICE","META-INF/NOTICE.txt","META-INF/ASL2.0"
        )
    }

}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.appcompat)
    implementation(libs.material)

    implementation("org.maplibre.gl:android-sdk:11.11.0")
    // ... deps lain

    implementation(libs.maplibre)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidxWorkRuntime)   // BENAR

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    implementation(libs.play.integrity)
}
