# Remnant - Built with Kotlin Multiplatform

This is a Kotlin Multiplatform project targeting Android, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here
  too.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

### Get SHA-1 Key

```shell
cd ~{jresource}/bin
./keytool -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore
```

### Creds

1. Add `google-services.json` to `Remnant/composeApp`
2. add `client_secret_desktop.json` to `Remnant/composeApp`
3. add `ktor-firebase-auth-firebase-adminsdk.json` to
   `Remnant/server/src/main/resources/ktor-firebase-auth-firebase-adminsdk.json`

### Stackoverflow refs

1. https://stackoverflow.com/questions/36919313/android-studio-issue-missing-missing-debug-keystore
2. https://stackoverflow.com/questions/27037194/keystore-file-doesnt-exist

### Docs

1. https://github.com/JetBrains/compose-multiplatform/blob/master/experimental/components/VideoPlayer
2. https://github.com/ktorio/ktor-documentation/tree/3.0.0/codeSnippets/snippets/auth-jwt-rs256
3. https://kb.vander.host/security/how-to-generate-rsa-public-and-private-key-pair-in-pkcs8-format/

### Reqs

#### _JVM DESKTOP_

1. requires ```libvlc```/```vlccore``` with VLC 3.x.x installed on ```linux```/```OSx``` respctively.
