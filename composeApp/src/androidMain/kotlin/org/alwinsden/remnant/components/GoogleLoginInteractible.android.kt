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
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.alwinsden.remnant.*
import org.alwinsden.remnant.api_data_class.AuthPost
import org.alwinsden.remnant.dataStore.coreComponent
import org.alwinsden.remnant.networking_utils.NetworkLogCodes
import org.alwinsden.remnant.networking_utils.onError
import org.alwinsden.remnant.networking_utils.onSuccess
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.android_dark_rd_4x
import javax.inject.Inject

suspend fun signInWithGoogleIdToken(idToken: String, nvvController: NavHostController) {
    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
    val firebaseAuthResult = Firebase.auth.signInWithCredential(firebaseCredential).await()
    val userIdToken = firebaseAuthResult.user?.getIdToken(true)?.await()?.token
    if (userIdToken != null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authRequest =
                    HTTP_CALL_CLIENT.authRequest(AuthPost(authCode = userIdToken, authMachine = "ANDROID"))
                withContext(Dispatchers.Main) {
                    authRequest.onSuccess {
                        coreComponent.appPreferences.addUpdateAuthKey(jwtToken = it.token)
                        val profileResponse = HTTP_CALL_CLIENT.profileGetRequest()
                        profileResponse.onSuccess {
                            if (it.demo_completed == true) {
                                nvvController.navigate(NavRouteClass.MainScreen1.route)
                            } else {
                                nvvController.navigate(NavRouteClass.EntryScreen1.route)
                            }
                        }.onError {
                            println("Failed to obtain user profile.")
                        }
                    }.onError {
                        Log.e(NetworkLogCodes.FailedAuth.code, "Failed the server authentication.")
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthNavigationError", "An error occurred: ${e.localizedMessage}")
            }
        }
    }
}

class RemnantViewModel @Inject constructor() : RemnantAppViewModal() {
    fun onSignInWithGoogle(credential: Credential, nvvController: NavHostController) {
        launchCatching {
            if (credential is CustomCredential) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                signInWithGoogleIdToken(
                    googleIdTokenCredential.idToken,
                    nvvController = nvvController
                )
            } else {
                Log.e(NetworkLogCodes.FailedAuth.code, "Failed to obtain google-sign in code.")
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
    val nvvController = LocalNavController.current
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
                    val requestSls = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    viewModel.onSignInWithGoogle(requestSls.credential, nvvController = nvvController)
                    Log.d(NetworkLogCodes.SuccessPing.code, "Credential obtained: ${requestSls.credential}")
                } catch (e: GetCredentialException) {
                    Log.d(NetworkLogCodes.FailedPing.code, e.message.orEmpty())
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
//    if (triggerLoadingState) {
//        Box(
//            modifier = Modifier
//                .width(280.dp)
//                .padding(top = 70.dp)
//        ) {
//            Text(
//                text = "Logging in. Please wait.",
//                color = Color.Red,
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//    }
}