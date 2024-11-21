package org.alwinsden.remnant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.DataStoreFactory
import com.google.api.client.util.store.FileDataStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alwinsden.remnant.HTTP_CALL_CLIENT
import org.alwinsden.remnant.LocalNavController
import org.alwinsden.remnant.NavRouteClass
import org.alwinsden.remnant.animationUtils.rememberVideoStatePlayer
import org.alwinsden.remnant.api_data_class.AuthPost
import org.alwinsden.remnant.dataStore.coreComponent
import org.alwinsden.remnant.networking_utils.onError
import org.alwinsden.remnant.networking_utils.onSuccess
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.android_dark_rd_4x
import java.awt.Desktop
import java.io.File
import java.io.InputStreamReader

val jsonFactory = GsonFactory.getDefaultInstance()
val CLIENT_SECRET_FILE = "client_secret_desktop.json"
val SCOPES: MutableCollection<String> =
    mutableListOf("https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile")
val DATA_STORE_DIR = File("tokens")
suspend fun signInWithGoogle(authCode: String?, nvvController: NavHostController) {
    withContext(Dispatchers.IO) {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val dataStoreFactory: DataStoreFactory = FileDataStoreFactory(DATA_STORE_DIR)

        // Load client secrets
        val clientSecrets =
            GoogleClientSecrets.load(jsonFactory, InputStreamReader(File(CLIENT_SECRET_FILE).inputStream()))

        // Build flow and trigger user authorization request
        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, clientSecrets, SCOPES
        )
            .setDataStoreFactory(dataStoreFactory)
            .setAccessType("offline")
            .build()

        if (authCode == null) {
            val authorizationUrl: String =
                flow.newAuthorizationUrl()
                    .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
                    .setScopes(listOf("openid", "profile", "email"))
                    .build()
            Desktop.getDesktop().browse(java.net.URI(authorizationUrl))
        } else {
            val tokenResponse: GoogleTokenResponse =
                flow.newTokenRequest(authCode).setRedirectUri("urn:ietf:wg:oauth:2.0:oob").execute()
            val credential: Credential = flow.createAndStoreCredential(tokenResponse, "user")
            val authRequestObject = HTTP_CALL_CLIENT.authRequest(
                AuthPost(
                    authMachine = "DESKTOP",
                    authCode = credential.accessToken
                )
            )
            withContext(Dispatchers.Main) {
                authRequestObject.onSuccess {
                    coreComponent.appPreferences.addUpdateAuthKey(jwtToken = it.token)
                    val profileGetRequestClient = HTTP_CALL_CLIENT.profileGetRequest()
                    profileGetRequestClient.onSuccess { ss ->
                        if (ss.demo_completed == true) {
                            nvvController.navigate(NavRouteClass.MainScreen1.route)
                        } else {
                            nvvController.navigate(NavRouteClass.EntryScreen1.route)
                        }
                    }.onError {
                        println("failed to obtain user profile.")
                    }
                }.onError {
                    println("server authentication failed.")
                }
            }
        }
    }
}


@Composable
actual fun GoogleLoginInteractible() {
    var showAuthInputDialog by remember { mutableStateOf(false) }
    var authCode by remember { mutableStateOf(TextFieldValue("")) }
    var isPromptLoading by remember { mutableStateOf(false) }
    val videoState = rememberVideoStatePlayer()
    val nvvController = LocalNavController.current
    OutlinedButton(
        onClick = {
            showAuthInputDialog = true
            CoroutineScope(Dispatchers.IO).launch {
                signInWithGoogle(null, nvvController)
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF000000)),
    ) {
//        VideoPlayer(
//            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//            onFinish = {},
//            state = videoState,
//            modifier = Modifier
//                .height(400.dp)
//                .width(
//                    400.dp
//                )
//        )
        Image(
            painter = painterResource(Res.drawable.android_dark_rd_4x),
            contentDescription = null,
            modifier = Modifier
                .width(250.dp)
        )
    }
    if (showAuthInputDialog) {
        AlertDialog(
            onDismissRequest = { showAuthInputDialog = false },
            title = {
                Text(
                    text = "Paste Authorization code below"
                )
            },
            text = {
                TextField(
                    value = authCode,
                    onValueChange = { authCode = it },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                    ),
                    modifier = Modifier
                        .border(border = BorderStroke(1.dp, Color.Gray))
                        .fillMaxWidth(),
                    maxLines = 3,
                    minLines = 3,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (authCode.text != "") {
                            isPromptLoading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                signInWithGoogle(authCode.text, nvvController)
                                withContext(Dispatchers.Main) {
                                    isPromptLoading = false
                                    showAuthInputDialog = false
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                ) {
                    if (isPromptLoading) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Text("logging in...")
                        }
                    } else {
                        Text("Submit")
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        System.exit(0)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults
                        .buttonColors(
                            backgroundColor = Color.LightGray
                        )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}