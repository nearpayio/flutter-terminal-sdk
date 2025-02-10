import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
    kotlin("kapt")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

android {
    namespace = "io.nearpay.terminalsdk"
    compileSdk = 35

    defaultConfig {
//        applicationId = "io.nearpay.softpos"
        minSdk = 28
        targetSdk = 34
//        versionCode = 1
//        versionName = "75"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "POS_BASE_URL", "\"https://sandbox-api.nearpay.io/\"")
        buildConfigField("String", "TRANSACTION_BASE_URL", "\"https://sandbox-pay-api.nearpay.io/\"")
        buildConfigField("String", "MTLS_POS_BASE_URL", "\"https://sa-sandbox-reader.nearpay.io/api/\"")
        buildConfigField("String", "MTLS_TRANSACTION_BASE_URL", "\"https://sa-sandbox-reader.nearpay.io/gateway/\"")
        buildConfigField("String", "WEBSOCKET_URL", "\"https://sandbox-api.nearpay.io/\"")

        buildConfigField ("String", "CLIENT_KEYSTORE_PRODUCTION", "\"${localProperties.getProperty("CLIENT_KEYSTORE_PRODUCTION")}\"")
        buildConfigField ("String", "CLIENT_KEYSTORE_SANDBOX", "\"${localProperties.getProperty("CLIENT_KEYSTORE_SANDBOX")}\"")
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
        sourceCompatibility = JavaVersion.
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(libs.cardreader.ui.mock.debug)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.mobileService.googleLocation)
    implementation(libs.mobileService.huaweiLocation)
    implementation(libs.mobileService.googlePlayIntegrity)
    implementation(libs.mobileService.huaweiSafetyDetect)
    implementation(libs.mobileService.huaweiAppService)
    implementation(libs.mobileService.googlePlayAppUpdate)
    implementation(libs.mobileService.googlePlayAppUpdateKtx)

    implementation(libs.androidX.workManager)

    implementation(libs.network.retrofit)
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttpSSE)
    implementation(libs.network.retrofitSerialization)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlin.reflection)
    api(libs.androidx.recyclerview)
    api(libs.mobileService.googleLocation)
    api(libs.mobileService.huaweiLocation)
//    implementation(libs.kotlin.stdlib){
//        because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
//    }

}

apply(from = "publish-terminalsdk.gradle")
