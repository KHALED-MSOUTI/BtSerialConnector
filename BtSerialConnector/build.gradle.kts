
buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()            // << --- ADD This
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.1.3")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    }
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")

}

android {
    namespace = "com.oetech.btserialconnector"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)        // << --- ADD This
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // RxJava is also required.
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
}




