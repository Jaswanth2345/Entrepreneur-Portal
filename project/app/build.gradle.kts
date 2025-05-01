import com.android.manifmerger.Actions.load
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.gms.google-services")
}


// Now define localProperties with explicit imports
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {
    namespace = "com.example.project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
//        val geminiApiKey: String = System.getenv("GEMINI_API_KEY") ?: project.findProperty("GEMINI_API_KEY") as? String ?: ""
        buildConfigField("String", "GEMINI_API_KEY", "\"" + (localProperties.getProperty("GEMINI_API_KEY") ?: "") + "\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.recyclerview:recyclerview:1.3.2") // Or latest version
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0") // Add Gemini SDK
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Needed for lifecycleScope
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth-ktx:22.1.1") // Use the latest version
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0") // Or latest

}