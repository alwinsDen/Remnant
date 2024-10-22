package org.alwinsden.remnant.dependencyInjection

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.alwinsden.remnant.shared.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val CLIENT_ID = BuildConfig.google_client_id
val appModule = module {
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .build()
        return GoogleSignIn.getClient(context, signInOptions)
    }
    single { getGoogleSignInClient(androidContext()) }
}