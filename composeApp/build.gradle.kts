import groovy.json.JsonSlurper
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    //added for firebase-auth
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            implementation(libs.bundles.ktor)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("com.google.api-client:google-api-client:2.7.0")
            implementation("com.google.oauth-client:google-oauth-client-jetty:1.36.0")
            implementation("com.google.api-client:google-api-client-gson:2.7.0")
            implementation("uk.co.caprica:vlcj:5.0.0-M1")
        }
    }
}

android {
    namespace = "org.alwinsden.remnant"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.alwinsden.remnant"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        //this is to process the Google services JSON
        val jsonFile = File("composeApp/google-services.json")
        //this condition was added because JVM desktop isn't able to detect the file.
        if (jsonFile.exists()) {
            val json = JsonSlurper().parse(jsonFile) as Map<*, *>
            val webClientId = (json["client"] as List<Map<*, *>>)
                .flatMap { it["oauth_client"] as List<Map<*, *>> }
                .find { it["client_type"] == 3 }?.get("client_id").toString()
            buildConfigField("String", "DEFAULT_WEB_CLIENT_ID", "\"$webClientId\"")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        implementation(libs.androidx.navigation.testing)
        implementation(libs.androidx.activity.ktx)
        debugImplementation(compose.uiTooling)
        androidTestImplementation("androidx.navigation:navigation-testing:2.8.3")
        implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
        implementation("com.google.firebase:firebase-auth")

        //Credential Manager Dependencies.
        implementation("com.google.android.gms:play-services-auth:21.2.0")
        implementation("androidx.credentials:credentials:1.5.0-alpha06")
        implementation("androidx.credentials:credentials-play-services-auth:1.5.0-alpha06")
        implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
        implementation("app.rive:rive-android:8.7.0")
        implementation("androidx.startup:startup-runtime:1.1.1")
    }
}

compose.desktop {
    application {
        mainClass = "org.alwinsden.remnant.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.alwinsden.remnant"
            packageVersion = "1.0.0"
        }
    }
}
