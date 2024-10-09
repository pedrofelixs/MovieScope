plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ahmedapps.moviesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ahmedapps.moviesapp"
        minSdk = 24
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependências do Android e Jetpack Compose
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")

    // Compose BOM (Bill of Materials) para garantir versões consistentes
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Dependências de Firebase (utilizando o BoM)
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))  // Adiciona o BoM para o Firebase
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("androidx.navigation:navigation-testing:2.8.2")  // Firebase Realtime Database

    // Dependências de Teste Unitário
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.1.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    androidTestImplementation("org.mockito:mockito-android:4.1.0")
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test:runner:1.5.2")


    // Room para persistência de dados
    implementation("androidx.room:room-ktx:2.6.0")
    androidTestImplementation("junit:junit:4.12")
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-paging:2.6.0")

    // Dagger Hilt para injeção de dependências
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Retrofit para requisições de API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coil para carregamento de imagens
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Extended Icons para Jetpack Compose
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // System UI Controller (Accompanist)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
}

apply(plugin = "com.google.gms.google-services")
