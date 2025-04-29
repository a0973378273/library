plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.dexatek.bluetooth"
    compileSdk = 35

    flavorDimensions += "lang"

    productFlavors {
        create("java") {
            dimension = "lang"
        }
        create("kotlin") {
            dimension = "lang"
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("java") {
            java.srcDirs("src/java")
        }
        getByName("kotlin") {
            java.srcDirs("src/kotlin")
//            java.srcDirs("src/main/java")
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
//    implementation(project(":bluetooth"))
//    implementation(project(":common"))
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Koin
    implementation(libs.bundles.koin)

    // Test
    androidTestImplementation(libs.bundles.test)
}