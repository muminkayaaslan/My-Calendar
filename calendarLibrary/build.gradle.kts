plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    `maven-publish`

}
group = "com.github.muminkayaaslan" // GitHub kullanıcı adını yaz
version = "1.0.0"

android {
    namespace = "com.aslansoft.calendarLibrary"
    version = "0.0.1-beta01"
    compileSdk = 35

    defaultConfig {
        minSdk = 30

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            // Yayınlanacak bileşeni belirtin (release veya debug)
            afterEvaluate {
                from(components["release"])
            }

            // Grup ID, Artifact ID ve versiyon bilgileri
            groupId = "com.github.muminkayaaslan" // GitHub kullanıcı adınız
            artifactId = "My-Calendar" // Repository adınız
            version = "1.0.0" // Kütüphanenizin versiyonu
        }
    }
}