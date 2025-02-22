package org.alwinsden.remnant.controlUtils

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.FileInputStream

fun Application.configurationFirebase() {
    val serviceAccount: FileInputStream =
        FileInputStream(System.getenv("FIREBASE_CREDENTIAL_PATH"))
    val firebaseOptions: FirebaseOptions = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(firebaseOptions)
}