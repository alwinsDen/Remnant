package org.alwinsden.remnant.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.alwinsden.remnant.BuildConfig
import org.alwinsden.remnant.RemnantAppViewModal
import org.alwinsden.remnant.networking.ApiCentral
import org.alwinsden.remnant.networking.createHttpClient
import org.alwinsden.remnant.networking_utils.onError
import org.alwinsden.remnant.networking_utils.onSuccess
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.android_dark_rd_4x
import javax.inject.Inject

suspend fun signInWithGoogleIdToken(idToken: String, client: ApiCentral) {
    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
    val firebaseAuthResult = Firebase.auth.signInWithCredential(firebaseCredential).await()
    val userIdToken = firebaseAuthResult.user?.getIdToken(true)?.await()?.token
    if (userIdToken != null) {
        Log.e("USER_ID_TOKEN", userIdToken)
        CoroutineScope(Dispatchers.IO).launch {
            client.testServerStatus()
                .onSuccess {
                    Log.e("SUCCESS_PING", it)
                }
                .onError {
                    Log.e("FAILED_PING", "FAILED_LOCALHOST_CALL")
                }
        }
    }
}

class RemnantViewModel @Inject constructor() : RemnantAppViewModal() {
    fun onSignInWithGoogle(credential: Credential) {
        launchCatching {
            if (credential is CustomCredential) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                signInWithGoogleIdToken(
                    googleIdTokenCredential.idToken, client =
                    ApiCentral(createHttpClient(OkHttp.create()))
                )
            } else {
                Log.e("FAILED_CREDS", "UNEXPECTED_CREDENTIALS")
            }
        }
    }
}

@Composable
actual fun GoogleLoginInteractible() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)
    val viewModel: RemnantViewModel = viewModel()
    OutlinedButton(
        onClick = {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.DEFAULT_WEB_CLIENT_ID)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            coroutineScope.launch {
                try {
                    val request = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    viewModel.onSignInWithGoogle(request.credential)
                    Log.d("GOOGLE_LOGIN", "Credential obtained: ${request.credential}")
                } catch (e: GetCredentialException) {
                    Log.d("FAILED", e.message.orEmpty())
                }
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF000000)),
    ) {
        Image(
            painter = painterResource(Res.drawable.android_dark_rd_4x),
            contentDescription = null,
            modifier = Modifier
                .width(250.dp)
        )
    }
}